package dataAccess;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import model.GameData;
import model.GameRequestData;

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
  public void joinGame(String username, GameRequestData data)
      throws DataAccessException {

    GameData targetGame = gameDB.get(data.gameID());
    if (targetGame == null) {
      throw new DataAccessException(400, "Error: bad request");
    }
    if (data.playerColor() == "WHITE") {
      if (targetGame.whiteUsername() != null) {
        throw new DataAccessException(403, "Error: already taken");
      } else {
        targetGame =
            new GameData(targetGame.gameID(), username,
                         targetGame.blackUsername(), targetGame.gameName());
      }
    }
    if (data.playerColor() == "BLACK") {
      if (targetGame.blackUsername() != null) {
        throw new DataAccessException(403, "Error: already taken");
      } else {
        targetGame =
            new GameData(targetGame.gameID(), targetGame.whiteUsername(),
                         username, targetGame.gameName());
      }
    }

    gameDB.remove(data.gameID());
    gameDB.put(targetGame.gameID(), targetGame);
  }

  @Override
  public void destroy() throws DataAccessException {
    gameDB.clear();
  }
}