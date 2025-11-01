package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTests {

  // ### TESTING SETUP/CLEANUP ###

  public static UserData newUser;
  private static UserDataAccess userDataAccess;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    userDataAccess = new UserSQLDAO();

  }

  @Test
  @Order(1)
  @DisplayName("Normal Create User")
  public void loginSuccess() throws DataAccessException {
    userDataAccess.destroy();
    UserData createdUser = userDataAccess.createUser(newUser);
    Assertions.assertEquals(createdUser.username(), newUser.username(),
        "User created successfully");
  };

  @Test
  @Order(2)
  @DisplayName("Null User")
  public void createUserNull() throws DataAccessException {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> userDataAccess.createUser(null));
  };

  @Test
  @Order(3)
  @DisplayName("Same username")
  public void createUserSameUsername() throws DataAccessException {
    userDataAccess.destroy();
    userDataAccess.createUser(newUser);
    Assertions.assertThrows(
        DataAccessException.class,
        () -> userDataAccess.createUser(newUser));
  };

  @Test
  @Order(4)
  @DisplayName("Normal Get User")
  public void getUserSuccess() throws DataAccessException {
    userDataAccess.destroy();
    UserData createdUser = userDataAccess.createUser(newUser);
    UserData foundUser = userDataAccess.getUser(newUser.username());
    Assertions.assertEquals(createdUser.username(), foundUser.username(),
        "User retrieved successfully");
  };

  @Test
  @Order(5)
  @DisplayName("No username")
  public void loginFailUsername() throws DataAccessException {
    Assertions.assertNull(
        userDataAccess.getUser("evilUser"), "Can't get user if don't exist");
  };
};
