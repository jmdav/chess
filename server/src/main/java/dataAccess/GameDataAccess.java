package dataAccess;

import model.UserData;

public interface GameDataAccess {

  UserData getUser(String username) throws DataAccessException;

  UserData createUser(UserData data) throws DataAccessException;

  void destroy() throws DataAccessException;
}