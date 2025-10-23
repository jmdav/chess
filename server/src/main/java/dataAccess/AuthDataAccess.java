package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

  AuthData createSession(String username) throws DataAccessException;

  AuthData getSession(String token) throws DataAccessException;
}
