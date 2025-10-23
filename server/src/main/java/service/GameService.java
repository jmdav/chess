package service;

import dataAccess.AuthDataAccess;
import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.GameRAMDAO;
import java.util.List;
import model.AuthData;
import model.GameData;
import model.GameID;
import model.GameList;
import model.GameRequestData;

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

  public GameList listGames(String authToken) throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      return gameAccess.getGames();
    } else {
      throw new DataAccessException(401, "Error: unauthorized");
    }
  }

  public GameID createGame(String authToken, String gameName)
      throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      return gameAccess.createGame(gameName);
    } else {
      throw new DataAccessException(401, "Error: unauthorized");
    }
  }

  public void joinGame(String authToken, GameRequestData request)
      throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      gameAccess.joinGame(session.username(), request);
    } else {
      throw new DataAccessException(401, "Error: unauthorized");
    }
  }
}
