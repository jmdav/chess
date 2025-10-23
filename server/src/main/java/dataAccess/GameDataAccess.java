package dataAccess;

import java.util.List;
import model.GameData;
import model.GameRequestData;

public interface GameDataAccess {

  List<GameData> getGames() throws DataAccessException;

  String createGame(String gameName) throws DataAccessException;

  void joinGame(String username, GameRequestData data)
      throws DataAccessException;

  void destroy() throws DataAccessException;
}