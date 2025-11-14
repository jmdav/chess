package client;

public class ServerFacade {
  public String register(SessionData data, String username, String password,
                         String email) {
    return "you exist";
  }
  public String login(SessionData data, String username, String password) {
    return "welcome you";
  }
  public String logout(SessionData data) { return "no more you"; }
  public String listGames(SessionData data) { return "unlimited bacon"; }
  public String joinGame(SessionData data, String gameID, String color) {
    return "unlimited bacon";
  }
  public String observeGame(SessionData data, String gameID) {
    return "unlimited bacon";
  }
  public String createGame(SessionData data, String gameName) {
    return "it's gamer time";
  }
}
