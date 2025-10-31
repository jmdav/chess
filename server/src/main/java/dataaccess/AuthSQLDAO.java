package dataaccess;

import java.sql.*;

import java.util.UUID;
import model.AuthData;

public class AuthSQLDAO implements AuthDataAccess {

  @Override
  public AuthData getSession(String token) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      System.out.println("Fetching session: " + token);
      var statement = "SELECT authToken, username FROM sessions WHERE authToken=?";
      try (PreparedStatement ps = conn.prepareStatement(statement)) {
        ps.setString(1, token);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return readSession(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(401,
          String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  private AuthData readSession(ResultSet rs) throws SQLException {
    AuthData session = new AuthData(
        rs.getString("authToken"),
        rs.getString("username"));
    return session;
  }

  @Override
  public AuthData createSession(String username) throws DataAccessException {
    var statement = "INSERT INTO sessions (authToken, username) VALUES (?, ?)";
    var authToken = UUID.randomUUID().toString();
    DatabaseManager.executeUpdate(statement, authToken, username);
    return new AuthData(authToken, username);
  };

  public AuthData deleteSession(String token) throws DataAccessException {
    var statement = "DELETE FROM sessions WHERE authToken=?";
    DatabaseManager.executeUpdate(statement, token);
    return null;
  };

  @Override
  public void destroy() throws DataAccessException {
    var statement = "TRUNCATE sessions";
    DatabaseManager.executeUpdate(statement);
  }
}