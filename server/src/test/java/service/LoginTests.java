package service;

import dataAccess.AuthRAMDAO;
import dataAccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;

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
    userService = new UserService(new AuthRAMDAO());
  }

  @BeforeEach
  public void setup() {}

  @Test
  @Order(1)
  @DisplayName("Normal Login")
  public void loginSuccess() throws DataAccessException {
    AuthData registerResult = userService.register(newUser);
    userService.logout(registerResult.authToken());
    AuthData loginResult = userService.login(newUser);
    Assertions.assertEquals(loginResult.username(), registerResult.username(),
                            "User logged in");
  };

  @Test
  @Order(2)
  @DisplayName("Wrong password")
  public void loginFailPassword() throws DataAccessException {
    userService.destroy();
    AuthData registerResult = userService.register(newUser);
    userService.logout(registerResult.authToken());
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