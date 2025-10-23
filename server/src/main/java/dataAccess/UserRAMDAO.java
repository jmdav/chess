package dataAccess;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.UserData;

public class UserRAMDAO implements UserDataAccess {

  private Map<String, UserData> userDB = new ConcurrentHashMap<>();

  @Override
  public UserData getUser(String username) throws DataAccessException {
    return userDB.get(username);
  };

  @Override
  public UserData createUser(UserData data) throws DataAccessException {
    userDB.put(data.username(), data);
    return data;
  };
}