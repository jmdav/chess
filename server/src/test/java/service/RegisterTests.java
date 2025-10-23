package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import handlers.*;
import java.net.HttpURLConnection;
import java.util.*;
import model.*;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTests {

  public static UserData newUser;
  private static UserService userService;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    userService = new UserService();
  }

  @BeforeEach
  public void setup() {}

  // Create User
  // Make already existing user

  @Test
  @Order(1)
  @DisplayName("Normal Login")
  public void loginSuccess() throws DataAccessException {
    AuthData registerResult = userService.register(newUser);
    Assertions.assertEquals(newUser.username(), registerResult.username(),
                            "Response did not give the same username as user");
    Assertions.assertNotNull(registerResult.token(),
                             "Response did not return authentication String");
  }

  @Test
  @Order(2)
  @DisplayName("User Already Exists")
  public void loginFail() throws DataAccessException {
    userService.register(newUser);
    Assertions.assertThrows(DataAccessException.class,
                            ()
                                -> userService.register(newUser),
                            "Reponse did not respect already created user");
  };
}