package client;

import client.errors.ResponseException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerConnector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  String serverUrl;
  String path;

  public ServerConnector(String serverUrl) throws ResponseException {
    this.serverUrl = serverUrl;
    this.path = "";
    get("/");
  }

  private String httpRequest(String path, String method, String data)
      throws ResponseException {

    String urlString = serverUrl + path;

    try {
      HttpRequest.Builder builder =
          HttpRequest.newBuilder()
              .uri(new URI(urlString))
              .timeout(java.time.Duration.ofMillis(5000));

      if (data != null) {
        builder.method(method.toUpperCase(),
                       HttpRequest.BodyPublishers.ofString(data));

      } else {
        builder.method(method.toUpperCase(),
                       HttpRequest.BodyPublishers.noBody());
      }
      HttpRequest request = builder.build();
      HttpResponse<String> httpResponse =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
        return httpResponse.body();
      } else {
        throw new ResponseException(httpResponse.body());
      }
    } catch (URISyntaxException e) {
      throw new ResponseException("Error: Invalid URL");
    } catch (IOException e) {
      throw new ResponseException("Error: Server failed to respond");
    } catch (InterruptedException e) {
      throw new ResponseException("Error: Server failed to respond");
    }
  }

  public String get(String path) throws ResponseException {
    return httpRequest(path, "GET", null);
  }

  public String post(String path, String data) throws ResponseException {
    return httpRequest(path, "POST", data);
  }

  public String delete(String path)throws ResponseException {
    return httpRequest(path, "DELETE", null);
  }
}
