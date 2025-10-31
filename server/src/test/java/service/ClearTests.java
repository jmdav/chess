package service;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import dataaccess.AuthRAMDAO;
import dataaccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearTests {

  // ### TESTING SETUP/CLEANUP ###

  public static UserData newUser;
  private static UserService userService;
  private static GameService gameService;
  public static AuthData spoofAuth;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    AuthRAMDAO authBase = new AuthRAMDAO();
    newUser = new UserData("clearUser", "newUserPassword", "nu@a.com");
    gameService = new GameService(authBase);
    userService = new UserService(authBase);

  }

  @Test
  @Order(1)
  @DisplayName("Clear")
  public void clear() throws DataAccessException {
    spoofAuth = userService.register(newUser);
    gameService.createGame(spoofAuth.authToken(), "test");
    gameService.destroy();
    Assertions.assertThrows(
        DataAccessException.class,
        () -> gameService.listGames(spoofAuth.authToken()).games(),
        "can't list games that have been Exploded");
  }
}
