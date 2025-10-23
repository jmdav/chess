package service;

import org.junit.jupiter.api.*;
import passoff.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameListTests {

  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    new TestUser("NewUser", "newUserPassword", "nu@mail.com");
  }

  @BeforeEach
  public void setup() {}

  // Gets a list of games
  // Unauthorized
}
