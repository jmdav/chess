package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDaoTests {

  // ### TESTING SETUP/CLEANUP ###

  private static GameDataAccess gameDataAccess;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    String newGame = "NewGame";
    gameDataAccess = new GameSQLDAO();
  }

  @Test
  @Order(1)
  @DisplayName("Normal Create Game")
  public void createGameSuccess() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame(newGame);
    Assertions.assertNotNull(createdGame, "Game created successfully");
  };

  @Test
  @Order(2)
  @DisplayName("Null Game")
  public void createGameNull() throws DataAccessException {
    Assertions.assertThrows(
        DataAccessException.class,
        () -> gameDataAccess.createGame(null));
  };

  @Test
  @Order(3)
  @DisplayName("Normal Get Game")
  public void getUserSuccess() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame(newGame);
    GameData foundGame = gameDataAccess.getGameData(createdGame.gameID());
    Assertions.assertEquals(foundGame.gameID, createdGame.gameID(),
        "Game retrieved successfully");
  };

  @Test
  @Order(4)
  @DisplayName("No game")
  public void getGameFail() throws DataAccessException {
    Assertions.assertThrows(
        DataAccessException.class,
        () -> gameDataAccess.getGameData(23489028), "Can't get game if don't exist");
  };

  @Test
  @Order(5)
  @DisplayName("Normal Get Games")
  public void getGamesSuccess() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame(newGame);
    GameList createdGames = gameDataAccess.getGames(newGame);
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };

  @Test
  @Order(6)
  @DisplayName("Games can have same name")
  public void canMakeManyGames() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame(newGame);
    GameID createdGame2 = gameDataAccess.createGame(newGame);
    GameList createdGames = gameDataAccess.getGames(newGame);
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };

    @Test
  @Order(7)
  @DisplayName("Can make many Games")
  public void canMakeManyGames() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame(newGame);
    GameID createdGame2 = gameDataAccess.createGame(newGame);
    GameID createdGame3 = gameDataAccess.createGame(newGame);
    GameID createdGame4 = gameDataAccess.createGame(newGame);
    GameID createdGame5 = gameDataAccess.createGame(newGame);
    GameList createdGames = gameDataAccess.getGames(newGame);
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };
};
