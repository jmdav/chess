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
      return null;
    }
  }
}
