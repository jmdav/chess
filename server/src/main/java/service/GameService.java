package service;

import dataAccess.AuthDataAccess;
import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import dataAccess.UserRAMDAO;
import model.AuthData;
import model.UserData;

public class UserService {

  private final UserDataAccess userAccess;
  private final AuthDataAccess authAccess;

  public UserService() {
    this.userAccess = new UserRAMDAO();
    this.authAccess = new AuthRAMDAO();
  }

  public AuthData register(UserData data) throws DataAccessException {
    if (userAccess.getUser(data.username()) == null) {
      userAccess.createUser(data);
      return authAccess.createSession(data.username());
    } else {
      throw new DataAccessException("Account already exists");
    }
  }

  public AuthData login(UserData data) throws DataAccessException {
    UserData prospectiveUser = userAccess.getUser(data.username());
    if (prospectiveUser != null) {
      if (prospectiveUser.password() == data.password())
        return authAccess.createSession(data.username());
    }
    throw new DataAccessException("Username or password incorrect");
  }

  public void logout(AuthData data) throws DataAccessException {
    AuthData session = authAccess.getSession(data.token());
    if (session != null) {
      authAccess.deleteSession(session.token());
      return;
    } else {
      throw new DataAccessException("Session does not exist");
    }
  }

  public void destroy() throws DataAccessException {
    authAccess.destroy();
    userAccess.destroy();
  }
}
