package dataAccess;

public interface DataAccess {

    authResult createAuth(authData data) throws dataAccess.DataAccessException;

    authData getSession(authToken data) throws dataAccess.DataAccessException;

}
