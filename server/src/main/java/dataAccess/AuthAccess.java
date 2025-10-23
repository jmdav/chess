package dataAccess;

import model.AuthData;

public interface AuthAccess {

  authResult createAuth(authData data) throws dataAccess.DataAccessException;

  authData getSession(authToken data) throws dataAccess.DataAccessException;
}
