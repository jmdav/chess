package service;

import model.*;
import org.junit.jupiter.api.*;

import dataaccess.AuthRAMDAO;
import dataaccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTests {

  public static UserData newUser;
  private static UserService userService;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    userService = new UserService(new AuthRAMDAO());
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
    Assertions.assertNotNull(registerResult.authToken(),
                             "Response did not return authentication String");
  }

  @Test
  @Order(2)
  @DisplayName("User Already Exists")
  public void loginFail() throws DataAccessException {
    userService.destroy();
    userService.register(newUser);
    Assertions.assertThrows(DataAccessException.class,
                            ()
                                -> userService.register(newUser),
                            "Reponse did not respect already created user");
  };
}