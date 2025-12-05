package dataaccess;

import model.GameID;
import model.GameList;
import model.GameRequestData;

public interface GameDataAccess {

  GameList getGames() throws DataAccessException;

  GameID createGame(String gameName) throws DataAccessException;

  void updateGame(Integer gameID, chess.ChessGame newGame) throws DataAccessException;

  void joinGame(String username, GameRequestData data)
      throws DataAccessException;

  void destroy() throws DataAccessException;
}