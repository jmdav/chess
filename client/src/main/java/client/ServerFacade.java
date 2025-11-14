package client;

public class ServerFacade {
  public String register(SessionData data, String username, String password,
                         String email) {
    return "you exist";
  }
  public String login(SessionData data, String username, String password) {
    return "welcome you";
  }
  public String listGames(SessionData data) { return "unlimited bacon"; }
  public String joinGame(SessionData data, String gameID) {
    return "unlimited bacon";
  }
}
