package dataaccess;

import java.sql.*;
import java.util.Properties;

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
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
                var preparedStatement = conn.prepareStatement(statement)) {
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
    }

    private final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS  users (
                      `username` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      `email` varchar(256) DEFAULT NULL,
                      PRIMARY KEY (`username`),
                      INDEX(username)
                    )
                    CREATE TABLE IF NOT EXISTS  sessions (
                      `authToken` varchar(256) NOT NULL,
                      `authData` varchar(256) NOT NULL,
                      PRIMARY KEY (`authToken`),
                      INDEX(authToken),
                      INDEX(authData)
                    )
                    CREATE TABLE IF NOT EXISTS  games (
                      `gameID` int NOT NULL AUTO_INCREMENT,
                      `whiteUsername` varchar(256) DEFAULT NULL,
                      `blackUsername` varchar(256) DEFAULT NULL,
                      `gameName` varchar(256) NOT NULL,
                      `gameData` varchar(256) DEFAULT NULL,
                      PRIMARY KEY (`gameID`),
                      INDEX(gameID),
                      INDEX(gameName)
                    )
                       ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """
    };

    private void configureDatabase() throws DataAccessException {
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

}
