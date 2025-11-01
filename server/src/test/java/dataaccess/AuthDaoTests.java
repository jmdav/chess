package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDaoTests {

  // ### TESTING SETUP/CLEANUP ###

  public static UserData newUser;
  public static AuthData spoofAuth;
  private static AuthDataAccess authBase;
  // ### TESTING SETUP/CLEANUP ###

  @BeforeAll
  public static void init() {

    newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    authBase = new AuthSQLDAO();
  }

  @Test
  @Order(1)
  @DisplayName("Normal Create Session")
  public void sessionSuccess() throws DataAccessException {
    authBase.destroy();
    AuthData createdAuth = authBase.createSession(newUser.username());
    Assertions.assertEquals(createdAuth.username(), newUser.username(),
        "Session created successfully");
  };

  @Test
  @Order(3)
  @DisplayName("Normal Get Session")
  public void getSessionSuccess() throws DataAccessException {
    AuthData createdSession = authBase.createSession(newUser.username());
    AuthData foundSession = authBase.getSession(createdSession.authToken());
    Assertions.assertEquals(createdSession.authToken(), foundSession.authToken(),
        "Session retrieved successfully");
  };

  @Test
  @Order(4)
  @DisplayName("No token")
  public void loginFailUsername() throws DataAccessException {
    Assertions.assertNull(
        authBase.getSession("evilUser"), "Can't get session if it doesn't exist");
  };

  @Test
  @Order(5)
  @DisplayName("Delete nonexistent Session")
  public void deleteNotExistSection() throws DataAccessException {
    Assertions.assertNull(
        authBase.deleteSession("evilUser"), "Can't delete session if it doesn't exist");
  };

  @Test
  @Order(6)
  @DisplayName("Delete Session")
  public void deleteSession() throws DataAccessException {
    AuthData createdSession = authBase.createSession(newUser.username());
    authBase.deleteSession(createdSession.authToken());
    Assertions.assertNull(
        authBase.getSession(createdSession.authToken()), "Can't get session if it doesn't exist");
  };
};
