package dataAccess;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
  public String createGame(String gameName) throws DataAccessException {
    GameData game =
        new GameData(UUID.randomUUID().toString(), null, null, gameName);
    gameDB.put(game.gameID(), game);
    return game.gameID();
  };

  @Override
  public void destroy() throws DataAccessException {
    gameDB.clear();
  }
}