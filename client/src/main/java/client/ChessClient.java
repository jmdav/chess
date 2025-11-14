package client;

import client.errors.ResponseException;
import java.util.Scanner;

public class ChessClient {

  private State state = State.SIGNEDOUT;
  private String username;
  private String out;
  private String in;

  public void run(String serverUrl) throws ResponseException {
    SessionData session = new SessionData(null, null);
    ServerFacade server = new ServerFacade(serverUrl);
    InputHandler input = new InputHandler(server);
    Scanner scanner = new Scanner(System.in);
    System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");

    while (state != State.QUIT) {

      System.out.print((username == null ? "" : "[" + username + "]") +
                       (" > "));
      in = scanner.nextLine();
      try {
        switch (state) {

        case SIGNEDOUT:
          out = input.parseSignedOut(session, in);
          break;

        case SIGNEDIN:
          out = input.parseSignedIn(session, in);
          break;

        case INGAME:
          out = input.parseInGame(session, in);
          break;

        case QUIT:
          break;
        }

      } catch (ResponseException exception) {
        out = exception.getErrorMessage();
      }

      System.out.println(out);
    }

    scanner.close();
  }
}
