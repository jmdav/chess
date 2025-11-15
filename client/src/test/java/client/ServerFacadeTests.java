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
  private static SessionData data = new SessionData();

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    try {
      facade = new ServerFacade("http://localhost:" + port);
    } catch (ResponseException e) {
      System.out.println("Tests failed before they even started...");
    }
  }

  @AfterAll
  static void stopServer() {
    server.stop();
  }

  @Test
  public void registerGood() throws ResponseException {
    data = facade.register(data, "test", "test", "test");
    Assertions.assertTrue(data.authToken() != null);
  }

  @Test
  public void registerExisting() {
    Assertions.assertThrows(
        ResponseException.class,
        () -> facade.register(data, "test", "test", "test"));
  }

  @Test
  public void logoutGood() throws ResponseException {
    String out = facade.logout(data);
    Assertions.assertTrue(out != null);
  }

  @Test
  public void logoutTwice() throws ResponseException {
    Assertions.assertThrows(ResponseException.class, () -> facade.logout(data));
  }

  @Test
  public void loginGood() throws ResponseException {
    data = facade.login(data, "test", "test");
    Assertions.assertTrue(data.authToken() != null);
  }

  @Test
  public void loginNotExist() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.login(data, "no", "notexist"));
  }

  @Test
  public void createGameGood() throws ResponseException {
    GameID game = facade.createGame(data, "the game");
    Assertions.assertTrue(game != null);
  }

  @Test
  public void createGameNoAuth() throws ResponseException {
    Assertions.assertThrows(
        ResponseException.class,
        () -> facade.createGame(new SessionData(), "the game"));
  }

  @Test
  public void listGamesGood() throws ResponseException {
    List<GameData> games = facade.listGames(data);
    Assertions.assertTrue(games.size() > 0);
  }

  @Test
  public void listGamesNoAuth() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.listGames(new SessionData()));
  }

  @Test
  public void joinGameGood() throws ResponseException {
    facade.joinGame(data, 1, TeamColor.BLACK);
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.joinGame(data, 1, TeamColor.BLACK));
  }

  @Test
  public void joinGameNotExist() throws ResponseException {
    Assertions.assertThrows(ResponseException.class,
                            () -> facade.joinGame(data, 100, TeamColor.BLACK));
  }
}
