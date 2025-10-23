package dataAccess;

import java.util.List;
import model.GameData;

public interface GameDataAccess {

  List<GameData> getGames() throws DataAccessException;

  GameData createGame() throws DataAccessException;

  void destroy() throws DataAccessException;
}