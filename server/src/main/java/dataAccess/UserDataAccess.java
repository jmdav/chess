package dataAccess;

import model.UserData;

public interface UserDataAccess extends AuthAccess {

  UserData getUser(String username) throws dataAccess.NoDataException;

  UserData createUser(UserData data) throws dataAccess.DataConflictException;
}