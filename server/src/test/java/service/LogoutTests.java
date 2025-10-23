package service;

import chess.ChessGame;
import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import handlers.*;
import java.net.HttpURLConnection;
import java.util.*;
import model.*;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogoutTests {

  public static UserData newUser;
  public static AuthData authSpoof;
  private static UserService userService;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    authSpoof = new AuthData("fart", "the fart token");
    userService = new UserService(new AuthRAMDAO());
  }

  @BeforeEach
  public void setup() {}

  @Test
  @Order(1)
  @DisplayName("Normal Logout")
  public void logoutSuccess() throws DataAccessException {
    AuthData registerResult = userService.register(newUser);
    userService.logout(registerResult.authToken());
    Assertions.assertThrows(
        DataAccessException.class,
        ()
            -> userService.logout(registerResult.authToken()),
        "Can't logout twice");
  };

  @Test
  @Order(2)
  @DisplayName("No session")
  public void loginFail() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class,
                            ()
                                -> userService.logout(authSpoof.authToken()),
                            "Can't logout if you don't exist");
  };
}