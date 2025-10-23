package service;

import dataAccess.AuthDataAccess;
import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.GameRAMDAO;
import java.util.List;
import model.AuthData;
import model.GameData;

public class GameService {

  private final GameDataAccess gameAccess;
  private final AuthDataAccess authAccess;

  public GameService(AuthRAMDAO authAccess) {
    this.gameAccess = new GameRAMDAO();
    this.authAccess = authAccess;
  }

  public void destroy() throws DataAccessException {
    authAccess.destroy();
    gameAccess.destroy();
  }

  public List<GameData> listGames(String authToken) throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      return gameAccess.getGames();
    } else {
      throw new DataAccessException("Unauthorized");
    }
  }

  public List<GameData> createGame(String authToken, GameData userData)
      throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      return gameAccess.createGame(userData);
    } else {
      throw new DataAccessException("Unauthorized");
    }
  }
}
