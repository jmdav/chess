package service;

import chess.ChessGame.TeamColor;
import dataaccess.AuthRAMDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameRequestData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateGameTests {

  private static GameService gameService;
  public static UserData newUser;
  public static AuthData spoofAuth;
  private static UserService userService;

  @BeforeAll
  public static void init() {

    newUser = new UserData("oldUser", "theFunk", "yfresh@mail.com");
    AuthRAMDAO authBase = new AuthRAMDAO();
    gameService = new GameService(authBase);
    userService = new UserService(authBase);

  }

  @Test
  @Order(1)
  @DisplayName("Make game")
  public void makeGame() throws DataAccessException {
    spoofAuth = userService.register(newUser);
    gameService.createGame(spoofAuth.authToken(), "t2est");
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
        () -> gameService.joinGame(spoofAuth.authToken(), request),
        "can't join game that don't exist");
  };
};
