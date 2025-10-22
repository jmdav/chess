package dataaccess;

public interface UserDataAccess extends DataAccess{

    userData getUser(String username) throws dataaccess.DataAccessException;

    userData createUser(userData data) throws dataaccess.DataAccessException;

}
