package dataAccess;

import model.UserData;

public interface UserDataAccess {

  UserData getUser(String username) throws DataAccessException;

  UserData createUser(UserData data) throws DataAccessException;
}