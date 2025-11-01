package service;

import chess.ChessGame.TeamColor;
import dataaccess.AuthSQLDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameID;
import model.GameRequestData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JoinGameTests {

  public static UserData userSpoof;
  private static GameService gameService;
  public static AuthData spoofAuth;
  private static UserService userService;

  @BeforeAll
  public static void init() {
    userSpoof = new UserData("userSpoof", "spoof", "nu@mail.com");
    AuthSQLDAO authBase = new AuthSQLDAO();
    userService = new UserService(authBase);
    gameService = new GameService(authBase);
  }

  @Test
  @Order(1)
  @DisplayName("Normal Join")
  public void joinGame() throws DataAccessException {
    userService.destroy();
    gameService.destroy();
    spoofAuth = userService.register(userSpoof);
    GameID game = gameService.createGame(spoofAuth.authToken(), "word");
    GameRequestData request = new GameRequestData(TeamColor.WHITE, game.gameID());
    gameService.joinGame(spoofAuth.authToken(), request);
    Assertions.assertEquals(gameService.listGames(spoofAuth.authToken())
        .games()
        .get(0)
        .whiteUsername(),
        spoofAuth.username(), "User logged in");
  };

  @Test
  @Order(2)
  @DisplayName("Game does not exist")
  public void joinBad() throws DataAccessException {
    gameService.destroy();
    userService.destroy();
    final AuthData spoofAuth = userService.register(userSpoof);
    // GameID game = gameService.createGame(spoofAuth.authToken(), "test");
    GameRequestData request = new GameRequestData(TeamColor.WHITE, 2);

    Assertions.assertThrows(
        DataAccessException.class,
        () -> gameService.joinGame(spoofAuth.authToken(), request),
        "can't join game that don't exist");
  };
};
