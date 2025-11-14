package client;

import client.errors.ResponseException;
import com.google.gson.Gson;
import model.*;

public class ServerFacade {

  private ServerConnector server;
  Gson serializer;
  String request;
  String response;
  ServerResponse output;

  public ServerFacade(String serverUrl) throws ResponseException {
    server = new ServerConnector(serverUrl);
    serializer = new Gson();
  }

  public ServerResponse register(SessionData data, String username,
                                 String password, String email)
      throws ResponseException {
    UserData newUser = new UserData(username, password, email);
    request = serializer.toJson(newUser);
    response = server.post("/user", request);
    AuthData newSession = serializer.fromJson(response, AuthData.class);
    output = new ServerResponse(
        new SessionData(newSession.authToken(), newSession.username(),
                        State.SIGNEDIN),
        "User " + newSession.username() + " registered successfully.");
    return output;
  }

  public ServerResponse login(SessionData data, String username,
                              String password) throws ResponseException {
    UserData newUser = new UserData(username, password, null);
    request = serializer.toJson(newUser);
    response = server.post("/session", request);
    AuthData newSession = serializer.fromJson(response, AuthData.class);
    output = new ServerResponse(
        new SessionData(newSession.authToken(), newSession.username(),
                        State.SIGNEDIN),
        "User " + newSession.username() + " logged in successfully.");
    return output;
  }

  public ServerResponse logout(SessionData data) throws ResponseException {
    return "no more you";
  }

  public ServerResponse listGames(SessionData data) throws ResponseException {
    return "unlimited bacon";
  }

  public ServerResponse joinGame(SessionData data, String gameID, String color)
      throws ResponseException {
    return "your bacon";
  }

  public ServerResponse observeGame(SessionData data, String gameID)
      throws ResponseException {
    return "their bacon";
  }

  public ServerResponse createGame(SessionData data, String gameName)
      throws ResponseException {
    return "it's gamer time";
  }
}
