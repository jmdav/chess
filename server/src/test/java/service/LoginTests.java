package service;

import chess.ChessGame;
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
public class LoginTests {

  public static UserData newUser;
  public static UserData evilUser;
  public static AuthData authSpoof;
  private static UserService userService;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    evilUser = new UserData("NewUser", "the fart password", "nu@mail.com");
    authSpoof = new AuthData("fart", "the fart token");
    userService = new UserService();
  }

  @BeforeEach
  public void setup() {}

  @Test
  @Order(1)
  @DisplayName("Normal Login")
  public void loginSuccess() throws DataAccessException {
    AuthData registerResult = userService.register(newUser);
    userService.logout(registerResult);
    AuthData loginResult = userService.login(newUser);
    Assertions.assertEquals(loginResult.username(), registerResult.username(),
                            "User logged in");
  };

  @Test
  @Order(2)
  @DisplayName("Wrong password")
  public void loginFailPassword() throws DataAccessException {
    AuthData registerResult = userService.register(newUser);
    userService.logout(registerResult);
    Assertions.assertThrows(
        DataAccessException.class,
        () -> userService.login(evilUser), "Wrong password");
  };

  @Test
  @Order(3)
  @DisplayName("No username")
  public void loginFailUsername() throws DataAccessException {
    Assertions.assertThrows(
        DataAccessException.class,
        () -> userService.login(evilUser), "Can't login if you don't exist");
  };
}