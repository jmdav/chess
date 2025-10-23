package dataAccess;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import model.GameData;

public class GameRAMDAO implements GameDataAccess {

  private Map<String, GameData> gameDB = new ConcurrentHashMap<>();

  @Override
  public List<GameData> getGames() throws DataAccessException {
    List<GameData> output = new Vector<>();
    gameDB.forEach((ID, data) -> { output.add(data); });
    return output;
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