package dataAccess;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.UserData;

public class GameRAMDAO implements UserDataAccess {

  private Map<String, UserData> gameDB = new ConcurrentHashMap<>();

  @Override
  public UserData getUser(String username) throws DataAccessException {
    return gameDB.get(username);
  };

  @Override
  public UserData createUser(UserData data) throws DataAccessException {
    gameDB.put(data.username(), data);
    return data;
  };

  @Override
  public void destroy() throws DataAccessException {
    gameDB.clear();
  }
}