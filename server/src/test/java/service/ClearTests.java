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
}
