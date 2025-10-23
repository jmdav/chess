package service;

import dataAccess.UserDataAccess;
import model.UserData;

public class UserService {

  private final UserDataAccess dataAccess;

  public UserService() { this.dataAccess = new UserDataAccess(); }

  public User register(UserData data) {
    if (dataAccess.getUser(data.username()) == null) {
      dataAccess.createUser(data);
      return dataAccess.createAuth(data.username());
    } else {
      return
    }
  }
}
