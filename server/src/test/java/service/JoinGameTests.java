package service;

import org.junit.jupiter.api.*;
import passoff.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JoinGameTests {

  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    // Make game, join game successfully
    // Game does not exist
  }
}
