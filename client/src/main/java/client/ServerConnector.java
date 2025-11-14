package client;

import client.errors.ResponseException;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import model.ErrorMessage;

public class ServerConnector {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  Gson serializer;
  String serverUrl;
  String path;

  public ServerConnector(String serverUrl) throws ResponseException {
    this.serverUrl = serverUrl;
    this.path = "";
    get("/", null);
    serializer = new Gson();
  }

  private String httpRequest(String path, String method, String data,
                             String authorization) throws ResponseException {

    String urlString = serverUrl + path;

    try {
      HttpRequest.Builder builder =
          HttpRequest.newBuilder()
              .uri(new URI(urlString))
              .timeout(java.time.Duration.ofMillis(5000));

      if (authorization != null) {
        builder.header("authorization", authorization);
      }

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
        ErrorMessage error =
            serializer.fromJson(httpResponse.body(), ErrorMessage.class);
        throw new ResponseException(error.message());
      }
    } catch (URISyntaxException e) {
      throw new ResponseException("Error: Invalid URL");
    } catch (IOException e) {
      throw new ResponseException("Error: Server failed to respond");
    } catch (InterruptedException e) {
      throw new ResponseException("Error: Server failed to respond");
    }
  }

  public String get(String path, String authorization)
      throws ResponseException {
    return httpRequest(path, "GET", null, authorization);
  }

  public String post(String path, String data, String authorization)
      throws ResponseException {
    return httpRequest(path, "POST", data, authorization);
  }

  public String delete(String path, String authorization)
      throws ResponseException {
    return httpRequest(path, "DELETE", null, authorization);
  }

  public String put(String path, String data, String authorization)
      throws ResponseException {
    return httpRequest(path, "PUT", null, authorization);
  }
}
