package client;

import chess.ChessGame;
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

  public void joinGame(SessionData data, Integer gameID,
                       ChessGame.TeamColor color) throws ResponseException {
    GameRequestData gameRequest = new GameRequestData(color, gameID);
    request = serializer.toJson(gameRequest);
    server.put("/game", request, data.authToken());
  }

  public String observeGame(SessionData data, String gameID)
      throws ResponseException {
    return "their bacon";
  }

  public GameID createGame(SessionData data, String gameName)
      throws ResponseException {
    GameStartData newGame = new GameStartData(gameName);
    request = serializer.toJson(newGame);
    response = server.post("/game", request, data.authToken());
    GameID createdGame = serializer.fromJson(response, GameID.class);
    return createdGame;
  }
}
