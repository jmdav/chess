package service;

import DataAccess.AuthRAMDAO;
import DataAccess.DataAccessException;
import chess.ChessGame.TeamColor;
import model.AuthData;
import model.GameRequestData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateGameTests {

  // ### TESTING SETUP/CLEANUP ###

  public static UserData newUser;
  public static AuthData spoofAuth;
  private static UserService userService;
  private static GameService gameService;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    AuthRAMDAO authBase = new AuthRAMDAO();
    userService = new UserService(authBase);
    gameService = new GameService(authBase);
  }

  @Test
  @Order(1)
  @DisplayName("Make game")
  public void makeGame() throws DataAccessException {
    spoofAuth = userService.register(newUser);
    gameService.createGame(spoofAuth.authToken(), "test");
    Assertions.assertNotNull(
        gameService.listGames(spoofAuth.authToken()).games().get(0),
        "yeppers... it's real");
  };

  @Test
  @Order(2)
  @DisplayName("Game does not exist")
  public void joinBad() throws DataAccessException {
    userService.destroy();
    final AuthData spoofAuth = userService.register(newUser);
    GameRequestData request = new GameRequestData(TeamColor.WHITE, 2);

    Assertions.assertThrows(
        DataAccessException.class,
        ()
            -> gameService.joinGame(spoofAuth.authToken(), request),
        "can't join game that don't exist");
  };
};
