package dataaccess;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
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
      throw new DataAccessException(401,
          String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  @Override
  public UserData createUser(UserData data) throws DataAccessException {
    var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    executeUpdate(statement, data.username(), data.password(), data.email());
    return new UserData(data.username(), data.password(), data.email());
  };

  @Override
  public void destroy() throws DataAccessException {
    var statement = "TRUNCATE users";
    executeUpdate(statement);
  }

  private UserData readUser(ResultSet rs) throws SQLException {
    UserData user = new UserData(
        rs.getString("username"),
        rs.getString("password"),
        rs.getString("email"));
    return user;
  }

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (int i = 0; i < params.length; i++) {
          Object param = params[i];
          if (param instanceof String p)
            ps.setString(i + 1, p);
          else if (param instanceof UserData p)
            ps.setString(i + 1, p.toString());
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