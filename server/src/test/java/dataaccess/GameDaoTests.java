package dataaccess;

import model.GameID;
import model.GameList;
import model.GameData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDaoTests {

  // ### TESTING SETUP/CLEANUP ###

  private static GameSQLDAO gameDataAccess;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    gameDataAccess = new GameSQLDAO();
  }

  @Test
  @Order(1)
  @DisplayName("Normal Create Game")
  public void createGameSuccess() throws DataAccessException {
    GameID createdGame = gameDataAccess.createGame("new game");
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
    GameID createdGame = gameDataAccess.createGame("new game");
    GameData foundGame = gameDataAccess.getGameData(createdGame.gameID());
    Assertions.assertEquals(foundGame.gameID(), createdGame.gameID(),
        "Game retrieved successfully");
  };

  @Test
  @Order(4)
  @DisplayName("No game")
  public void getGameFail() throws DataAccessException {
    Assertions.assertNull(gameDataAccess.getGameData(23489028), "Can't get game if don't exist");
  };

  @Test
  @Order(5)
  @DisplayName("Normal Get Games")
  public void getGamesSuccess() throws DataAccessException {
    gameDataAccess.createGame("new game");
    GameList createdGames = gameDataAccess.getGames();
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };

  @Test
  @Order(6)
  @DisplayName("Games can have same name")
  public void canDuplicateName() throws DataAccessException {
    gameDataAccess.createGame("new game");
    gameDataAccess.createGame("new game");
    GameList createdGames = gameDataAccess.getGames();
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };

  @Test
  @Order(7)
  @DisplayName("Can make many Games")
  public void canMakeManyGames() throws DataAccessException {
    gameDataAccess.createGame("new game");
    gameDataAccess.createGame("new game");
    gameDataAccess.createGame("new game");
    gameDataAccess.createGame("new game");
    gameDataAccess.createGame("new game");
    GameList createdGames = gameDataAccess.getGames();
    Assertions.assertNotNull(createdGames, "Games retrieved successfully");
  };
};
