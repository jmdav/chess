package service;

import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameListTests {

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
  @DisplayName("Normal List")
  public void getList() throws DataAccessException {
    spoofAuth = userService.register(newUser);
    gameService.createGame(spoofAuth.authToken(), "test");
    Assertions.assertNotNull(
        gameService.listGames(spoofAuth.authToken()).games());
  };

  @Test
  @Order(2)
  @DisplayName("No permission")
  public void noPermission() throws DataAccessException {

    Assertions.assertThrows(
        DataAccessException.class,
        ()
            -> gameService.listGames(
                "I DO NOT HAVE PERMISSION TO ACCESS THE LIST OF GAMES"),
        "he doesn't have permission...");
  };
};
