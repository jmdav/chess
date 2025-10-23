package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

  AuthData createSession(String username) throws DataAccessException;

  AuthData getSession(String token) throws DataAccessException;

  AuthData deleteSession(String token) throws DataAccessException;

  void destroy() throws DataAccessException;
}
