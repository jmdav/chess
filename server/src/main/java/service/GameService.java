package service;

import chess.ChessGame.TeamColor;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.GameSQLDAO;
import model.AuthData;
import model.GameData;
import model.GameID;
import model.GameList;
import model.GameRequestData;

public class GameService {

  private final GameDataAccess gameAccess;
  private final AuthDataAccess authAccess;

  public GameService(AuthDataAccess authAccess) {
    this.gameAccess = new GameSQLDAO();
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

  public void updateGameData(Integer gameID, chess.ChessGame game) throws DataAccessException {
    gameAccess.updateGame(gameID, game);
  }

  public GameData getGameById(Integer gameID) throws IndexOutOfBoundsException, DataAccessException {
    GameList gameList = gameAccess.getGames();
    GameData gameData;
    try {
      gameData = gameList.games().get(gameID - 1);
    } catch (IndexOutOfBoundsException e) {
      throw new IndexOutOfBoundsException();
    }
    if (gameData != null) {
      return gameData;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public TeamColor getColorByUsername(GameData gameData, String username) {
    if (gameData.whiteUsername() != null &&
        gameData.whiteUsername().equals(username)) {
      return TeamColor.WHITE;
    } else if (gameData.blackUsername() != null &&
        gameData.blackUsername().equals(username)) {
      return TeamColor.BLACK;
    } else {
      return null;
    }
  }

  public GameID createGame(String authToken, String gameName)
      throws DataAccessException {
    if (gameName == null) {
      throw new DataAccessException(400, "Error: bad request");
    }
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

  public void leaveGame(String authToken, GameRequestData request)
      throws DataAccessException {
    AuthData session = authAccess.getSession(authToken);
    if (session != null) {
      gameAccess.joinGame(null, request);
    } else {
      throw new DataAccessException(401, "Error: unauthorized");
    }
  }
}
