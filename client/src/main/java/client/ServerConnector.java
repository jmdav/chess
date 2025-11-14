package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerConnector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  String urlString;
  String host;
  int port;
  String path;

  public ServerConnector(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public String get(String path) {
    String urlString =
        String.format(Locale.getDefault(), "http://%s:%d%s", host, port, path);

    HttpRequest request = HttpRequest.newBuilder()
                              .uri(new URI(urlString))
                              .timeout(java.time.Duration.ofMillis(5000))
                              .GET()
                              .build();

    HttpResponse<String> httpResponse =
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
      return httpResponse.body();
    } else {
      System.out.println("Error: received status code " +
                         httpResponse.statusCode());
    }
  }

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
    return "your bacon";
  }
  public String observeGame(SessionData data, String gameID) {
    return "their bacon";
  }
  public String createGame(SessionData data, String gameName) {
    return "it's gamer time";
  }
}
