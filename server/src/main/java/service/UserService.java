package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.UserSQLDAO;
import model.AuthData;
import model.UserData;

public class UserService {

  private final UserDataAccess userAccess;
  private final AuthDataAccess authAccess;

  public UserService(AuthDataAccess authAccess) {
    this.userAccess = new UserSQLDAO();
    this.authAccess = authAccess;
  }

  public AuthData register(UserData data) throws DataAccessException {
    if (data.username() == null || data.password() == null) {
      throw new DataAccessException(400, "Error: bad request");
    }
    if (userAccess.getUser(data.username()) == null) {
      userAccess.createUser(data);
      return authAccess.createSession(data.username());
    } else {
      throw new DataAccessException(403, "Error: already taken");
    }
  }

  public AuthData login(UserData data) throws DataAccessException {
    if (data.username() != null && data.password() != null) {
      UserData prospectiveUser = userAccess.getUser(data.username());
      // System.out.println(prospectiveUser);
      // System.out.println(data);
      if (prospectiveUser != null &&
          prospectiveUser.password().equals(data.password())) {
        return authAccess.createSession(data.username());
      } else {
        throw new DataAccessException(401, "Error: unauthorized");
      }
    } else {
      throw new DataAccessException(400, "Error: bad request");
    }
  }

  public void logout(String authToken) throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      authAccess.deleteSession(session.authToken());
    } else {
      throw new DataAccessException(401, "Error: unauthorized");
    }
  }

  public void destroy() throws DataAccessException {
    authAccess.destroy();
    userAccess.destroy();
  }
}
