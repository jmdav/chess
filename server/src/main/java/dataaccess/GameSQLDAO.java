package dataaccess;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import model.GameData;
import model.GameID;
import model.GameList;
import model.GameRequestData;

public class GameSQLDAO implements GameDataAccess {

  @Override
  public GameList getGames() throws DataAccessException {
    var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, "
                    + "chessGame FROM games";
    List<GameData> output = new Vector<>();
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(statement);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        output.add(readGame(rs));
      }
      return new GameList(output);
    } catch (Exception e) {
      throw new DataAccessException(
          500, String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  private GameData readGame(ResultSet rs) throws SQLException {
    ChessGame game =
        new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
    GameData output = new GameData(
        rs.getInt("gameID"), rs.getString("whiteUsername"),
        rs.getString("blackUsername"), rs.getString("gameName"), game);
    return output;
  }

  public void updateGame(Integer gameID, ChessGame newGame)
      throws DataAccessException {
    var statement = "UPDATE games SET chessGame=? WHERE gameID=?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(statement)) {
      ps.setString(1, new Gson().toJson(newGame));
      ps.setInt(2, gameID);
      ps.executeUpdate();
      // System.out.println("Game data updated: " + gameID + "\n" +
      // newGame.toString());
    } catch (SQLException e) {
      throw new DataAccessException(
          500, String.format("Unable to update game: %s", e.getMessage()));
    }
  }

  @Override
  public GameID createGame(String gameName) throws DataAccessException {
    var statement = "INSERT INTO games (gameName, chessGame) VALUES (?, ?)";
    String gameJson = new Gson().toJson(new ChessGame());
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             statement, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, gameName);
      ps.setString(2, gameJson);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          int gameID = rs.getInt(1);
          return new GameID(gameID);
        } else {
          throw new DataAccessException(
              500, "Unable to retrieve generated game ID.");
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException(
          500, String.format("Unable to create game: %s", e.getMessage()));
    }
  }

  @Override
  public void joinGame(String username, GameRequestData data)
      throws DataAccessException {

    GameData targetGame = getGameData(data.gameID());

    if (data.gameID() == null || targetGame == null) {
      throw new DataAccessException(400, "Error: bad request");
    }
    // Check if team color is either white or black
    if ((data.playerColor() != TeamColor.WHITE &&
         data.playerColor() != TeamColor.BLACK)) {
      throw new DataAccessException(400, "Error: bad request");
    }
    // Check if white is already taken
    if (data.playerColor() == TeamColor.WHITE) {
      if (targetGame.whiteUsername() != null && username != null &&
          targetGame.whiteUsername() != username) {
        throw new DataAccessException(403, "Error: already taken");
      } else {
        targetGame = new GameData(targetGame.gameID(), username,
                                  targetGame.blackUsername(),
                                  targetGame.gameName(), targetGame.game());
      }
    }
    // Check if black is already taken
    else if (data.playerColor() == TeamColor.BLACK && username != null &&
             targetGame.whiteUsername() != username) {
      if (targetGame.blackUsername() != null &&
          targetGame.blackUsername().equals(username) == false) {
        throw new DataAccessException(403, "Error: already taken");
      } else {
        targetGame =
            new GameData(targetGame.gameID(), targetGame.whiteUsername(),
                         username, targetGame.gameName(), targetGame.game());
      }
    }

    var statement =
        "UPDATE games SET whiteUsername=?, blackUsername=? WHERE gameID=?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(statement)) {
      if (targetGame.whiteUsername() != null) {
        ps.setString(1, targetGame.whiteUsername());
      } else {
        ps.setNull(1, Types.VARCHAR);
      }
      if (targetGame.blackUsername() != null) {
        ps.setString(2, targetGame.blackUsername());
      } else {
        ps.setNull(2, Types.VARCHAR);
      }
      ps.setInt(3, targetGame.gameID());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(
          500, String.format("Unable to update game: %s", e.getMessage()));
    }
  }

  @Override
  public void destroy() throws DataAccessException {
    var statement = "TRUNCATE games";
    DatabaseManager.executeUpdate(statement);
  }

  public GameData getGameData(Integer gameID) throws DataAccessException {
    if (gameID == null) {
      return null;
    }
    try (Connection conn = DatabaseManager.getConnection()) {
      System.out.println("Fetching game data: " + gameID);
      var statement =
          "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame "
          + "FROM games WHERE gameID=?";
      try (PreparedStatement ps = conn.prepareStatement(statement)) {
        ps.setInt(1, gameID);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGame(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(
          500, String.format("Error: Unable to read data: %s", e.getMessage()));
    }
    return null;
  }
}