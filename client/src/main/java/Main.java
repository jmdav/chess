import client.ChessClient;

public class Main {
  public static void main(String[] args) {
    String serverUrl = "http://localhost:8080";
    if (args.length == 1) {
      serverUrl = args[0];
    }
    System.out.println(serverUrl);
    try {
      new ChessClient().run(serverUrl);
    } catch (Throwable ex) {
      System.out.println("Unable to connect to server");
      System.out.println(ex.getMessage());
    }
  }
}