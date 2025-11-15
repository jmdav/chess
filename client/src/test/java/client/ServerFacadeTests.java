package client;

import chess.ChessGame.TeamColor;
import client.errors.ResponseException;
import java.util.List;
import model.GameData;
import model.GameID;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {

  private static Server server;
  private static ServerFacade facade;
  private static SessionData data;

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    try {
      facade = new ServerFacade("http://localhost:" + port);
      data = new SessionData(null, null, State.SIGNEDOUT);
      facade.destroy();
    } catch (ResponseException e) {
      System.out.println("Tests failed before they even started...");
    }
  }

  @AfterAll
  public static void stopServer() {
    server.stop();
  }

  @Test
  @Order(1)
  public void registerGood() throws ResponseException {
    facade.destroy();
    data = facade.register(data, "test", "test", "test");
    Assertions.assertTrue(data.authToken() != null);
    // Assertions.assertTrue(true);
  }

  @Test
  @Order(2)
  public void registerExisting() throws ResponseException {
    facade.destroy();
    data = facade.register(data, "test", "test", "test");
    Assertions.assertThrows(
        ResponseException.class,
        () -> facade.register(data, "test", "test", "test"));
  }

  @Test
  @Order(3)
  public void logoutGood() throws ResponseException {
    String out = facade.logout(data);
    Assertions.assertTrue(out != null);
  }

  @Test
  @Order(4)
  public void logoutTwice() throws ResponseException {
    facade.destroy();
    Assertions.assertThrows(ResponseException.class, () -> facade.logout(data));
  }

  @Test
  @Order(5)
  public void loginGood() throws ResponseException {
    data = facade.register(data, "test", "test", "test");
    facade.logout(data);
    data = facade.login(data, "test", "test");
    Assertions.assertTrue(data.authToken() != null);
  }

  @Test
  @Order(6)
  public void loginNotExist() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.login(data, "no", "notexist"));
  }

  @Test
  @Order(7)
  public void createGameGood() throws ResponseException {
    GameID game = facade.createGame(data, "the game");
    Assertions.assertTrue(game != null);
  }

  @Test
  @Order(8)
  public void createGameNoAuth() throws ResponseException {
    Assertions.assertThrows(
        ResponseException.class,
        ()
            -> facade.createGame(new SessionData(null, null, State.SIGNEDIN),
                                 "the game"));
  }

  @Test
  @Order(9)
  public void listGamesGood() throws ResponseException {
    facade.destroy();
    data = facade.register(data, "test", "test", "test");
    facade.createGame(data, "the game");
    List<GameData> games = facade.listGames(data);
    Assertions.assertTrue(games.size() > 0);
  }

  @Test
  @Order(10)
  public void listGamesNoAuth() throws ResponseException {
    Assertions.assertThrows(
        ResponseException.class,
        () -> facade.listGames(new SessionData(null, null, State.SIGNEDIN)));
  }

  @Test
  @Order(11)
  public void joinGameGood() throws ResponseException {
    facade.destroy();
    data = facade.register(data, "test", "test", "test");
    facade.createGame(data, "the game");
    facade.joinGame(data, 1, TeamColor.BLACK);
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.joinGame(data, 1, TeamColor.BLACK));
  }

  @Test
  @Order(12)
  public void joinGameNotExist() throws ResponseException {
    facade.destroy();
    data = facade.register(data, "test", "test", "test");
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.joinGame(data, 100, TeamColor.BLACK));
  }
}
