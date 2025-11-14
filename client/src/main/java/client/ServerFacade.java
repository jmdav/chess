package client;

import client.errors.ResponseException;
import com.google.gson.Gson;
import java.util.List;
import model.*;

public class ServerFacade {

  private ServerConnector server;
  Gson serializer;
  String request;
  String response;

  public ServerFacade(String serverUrl) throws ResponseException {
    server = new ServerConnector(serverUrl);
    serializer = new Gson();
  }

  public SessionData register(SessionData data, String username,
                              String password, String email)
      throws ResponseException {
    UserData newUser = new UserData(username, password, email);
    request = serializer.toJson(newUser);
    response = server.post("/user", request, null);
    AuthData newSession = serializer.fromJson(response, AuthData.class);
    SessionData output = new SessionData(newSession.authToken(),
                                         newSession.username(), State.SIGNEDIN);
    return output;
  }

  public SessionData login(SessionData data, String username, String password)
      throws ResponseException {
    UserData newUser = new UserData(username, password, null);
    request = serializer.toJson(newUser);
    response = server.post("/session", request, null);
    AuthData newSession = serializer.fromJson(response, AuthData.class);
    SessionData output = new SessionData(newSession.authToken(),
                                         newSession.username(), State.SIGNEDIN);
    return output;
  }

  public String logout(SessionData data) throws ResponseException {
    return server.delete("/session", data.authToken());
  }

  public List<GameData> listGames(SessionData data) throws ResponseException {
    response = server.get("/game", data.authToken());
    GameList games = serializer.fromJson(response, GameList.class);
    return games.games();
  }

  public String joinGame(SessionData data, String gameID, String color)
      throws ResponseException {
    return "your bacon";
  }

  public String observeGame(SessionData data, String gameID)
      throws ResponseException {
    return "their bacon";
  }

  public GameData createGame(SessionData data, String gameName)
      throws ResponseException {
    GameStartData newGame = new GameStartData(gameName);
    request = serializer.toJson(newGame);
    response = server.post("/game", request, data.authToken());
    GameData createdGame = serializer.fromJson(response, GameData.class);
    return createdGame;
  }
}
