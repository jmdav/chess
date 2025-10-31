package dataaccess;

import java.sql.*;

public class SqlManager {

    static public ResultSet executeQuery(String query) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                var result = preparedStatement.executeQuery();
                return result;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(401,
                    String.format("Unable to query database: %s", ex.getMessage()));
        }
    }

    static public int executeUpdate(String query) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                var result = preparedStatement.executeUpdate(query);
                return result;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(401,
                    String.format("Unable to query database: %s", ex.getMessage()));
        }
    }

}