package client;

import client.errors.ResponseException;

public class ServerFacade {

  private ServerConnector server;

  public ServerFacade(String serverUrl) throws ResponseException {
    server = new ServerConnector(serverUrl);
  }

  public String register(SessionData data, String username, String password,
                         String email) throws ResponseException {
    return server.post("/register", username);
  }
  public String login(SessionData data, String username, String password) {
    return "welcome you";
  }
  public String logout(SessionData data) throws ResponseException {
    return "no more you";
  }
  public String listGames(SessionData data) throws ResponseException {
    return "unlimited bacon";
  }
  public String joinGame(SessionData data, String gameID, String color)
      throws ResponseException {
    return "your bacon";
  }
  public String observeGame(SessionData data, String gameID)
      throws ResponseException {
    return "their bacon";
  }
  public String createGame(SessionData data, String gameName)
      throws ResponseException {
    return "it's gamer time";
  }
}
