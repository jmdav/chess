package service;

import chess.ChessGame.TeamColor;
import dataAccess.AuthDataAccess;
import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameID;
import model.GameRequestData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JoinGameTests {

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
  @DisplayName("Normal Join")
  public void joinGame() throws DataAccessException {
    spoofAuth = userService.register(newUser);
    GameID game = gameService.createGame(spoofAuth.authToken(), "gamename");
    GameRequestData request =
        new GameRequestData(TeamColor.WHITE, game.gameID());
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
    final AuthData spoofAuth = userService.register(newUser);
    // GameID game = gameService.createGame(spoofAuth.authToken(), "test");
    GameRequestData request = new GameRequestData(TeamColor.WHITE, 2);

    Assertions.assertThrows(
        DataAccessException.class,
        ()
            -> gameService.joinGame(spoofAuth.authToken(), request),
        "can't join game that don't exist");
  };
};
