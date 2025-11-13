package dataaccess;

import java.sql.*;

import model.UserData;

public class UserSQLDAO implements UserDataAccess {

  @Override
  public UserData getUser(String username) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      System.out.println("Fetching user: " + username);
      var statement = "SELECT username, password, email FROM users WHERE username=?";
      try (PreparedStatement ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return readUser(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(500,
          String.format("Error: Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  @Override
  public UserData createUser(UserData data) throws DataAccessException {
    var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    DatabaseManager.executeUpdate(statement, data.username(), data.password(), data.email());
    return new UserData(data.username(), data.password(), data.email());
  };

  @Override
  public void destroy() throws DataAccessException {
    var statement = "TRUNCATE users";
    DatabaseManager.executeUpdate(statement);
  }

  private UserData readUser(ResultSet rs) throws SQLException {
    UserData user = new UserData(
        rs.getString("username"),
        rs.getString("password"),
        rs.getString("email"));
    return user;
  }

}