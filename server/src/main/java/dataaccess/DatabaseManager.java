package dataaccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    public DatabaseManager() throws DataAccessException {
        configureDatabase();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        System.out.println("Requesting to create database...");
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
                var preparedStatement = conn.prepareStatement(statement)) {
            System.out.println("Creating database...");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(400, ex.getMessage());
        }
    }

    static public void destroyDatabase() throws DataAccessException {
        System.out.println("Requesting to destroy database...");
        var statement = "DROP DATABASE IF EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
                var preparedStatement = conn.prepareStatement(statement)) {
            System.out.println("Destroying database...");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(400, ex.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            // do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException(400, "failed to get connection");
        }
    }

    private static void loadPropertiesFromResources() {
        System.out.println("Loading database properties...");
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
        System.out.println("Database properties loaded: " + connectionUrl);
    }

    public static final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS users (
                      `username` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      `email` varchar(256) DEFAULT NULL,
                      PRIMARY KEY (`username`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """,
            """
                    CREATE TABLE IF NOT EXISTS sessions (
                      `authToken` varchar(256) NOT NULL,
                      `username` varchar(256) NOT NULL,
                      PRIMARY KEY (`authToken`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """,
            """
                    CREATE TABLE IF NOT EXISTS games (
                      `gameID` INT NOT NULL AUTO_INCREMENT,
                      `whiteUsername` varchar(256) DEFAULT NULL,
                      `blackUsername` varchar(256) DEFAULT NULL,
                      `gameName` varchar(256) NOT NULL,
                      `gameData` varchar(256) DEFAULT NULL,
                      PRIMARY KEY (`gameID`),
                      INDEX (gameName)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """
    };

    public static void configureDatabase() throws DataAccessException {
        System.out.println("Configuring database...");
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(404,
                    String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p)
                        ps.setString(i + 1, p);
                    else if (param == null)
                        ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(402,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

}
