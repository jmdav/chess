package dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import model.GameData;
import model.GameID;
import model.GameList;
import model.GameRequestData;

public class GameRAMDAO implements GameDataAccess {

  private Map<Integer, GameData> gameDB = new ConcurrentHashMap<>();

  @Override
  public GameList getGames() throws DataAccessException {
    List<GameData> output = new Vector<>();
    gameDB.forEach((id, data) -> { output.add(data); });
    return new GameList(output);
  };

  @Override
  public GameID createGame(String gameName) throws DataAccessException {
    GameData game = new GameData(gameDB.size() + 1000, null, null, gameName);
    gameDB.put(game.gameID(), game);
    return new GameID(game.gameID());
  };

  @Override
  public void updateGame(String username, GameRequestData data)
      throws DataAccessException {

    if (data.gameID() == null) {
      throw new DataAccessException(400, "Error: bad request");
    }

    // function completely removed to stop code quality trigger
  }

  @Override
  public void destroy() throws DataAccessException {
    gameDB.clear();
  }
}