package dataAccess;

import java.util.List;
import model.GameData;

public interface GameDataAccess {

  List<GameData> getGames() throws DataAccessException;

  String createGame(String gameName) throws DataAccessException;

  void destroy() throws DataAccessException;
}