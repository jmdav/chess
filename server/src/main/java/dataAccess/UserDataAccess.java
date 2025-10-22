package dataAccess;

public interface UserDataAccess extends DataAccess{

    userData getUser(String username) throws dataAccess.DataAccessException;

    userData createUser(userData data) throws dataAccess.DataAccessException;

}
