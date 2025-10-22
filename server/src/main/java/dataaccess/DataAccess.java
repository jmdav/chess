package dataaccess;

public interface DataAccess {

    authResult createAuth(authData);

    authData getSession(authToken);

}
