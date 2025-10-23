package service;

import chess.ChessGame;
import java.net.HttpURLConnection;
import java.util.*;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearTests {

  private static TestUser existingUser;
  private static TestUser newUser;
  private String existingAuth;

  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    existingUser =
        new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
  }

  @BeforeEach
  public void setup() {}

  // Clear, is database clear

  @Test
  @Order(1)
  @DisplayName("Static Files")
  public void staticFilesSuccess() {
    Assertions.assertEquals();
  }

  @Test
  @Order(2)
  @DisplayName("Normal User Login")
  public void loginSuccess() {
    Assertions.assertNotNull(loginResult.getAuthToken(),
                             "Response did not return authentication String");
  }

  @Test
  @Order(3)
  @DisplayName("Normal User Login")
  public void assertAuthFieldsMissing() {
    Assertions.assertNull(result.getUsername(),
                          "Response incorrectly returned username");
    Assertions.assertNull(result.getAuthToken(),
                          "Response incorrectly return authentication String");
  }
}
