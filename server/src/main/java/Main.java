import chess.*;
import java.util.UUID;
import server.Server;

public class Main {
  public static void main(String[] args) {
    Server server = new Server();
    server.run(8080);
    System.out.println("♕ 240 Chess Server: " + generateToken());
  }

  public static String generateToken() { return UUID.randomUUID().toString(); }
}
