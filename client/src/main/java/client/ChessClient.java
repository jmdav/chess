package client;

import client.errors.ResponseException;
import java.util.Scanner;

public class ChessClient {

  private HandlerResponse response;
  private String in;

  public void run(String serverUrl) throws ResponseException {
    SessionData session = new SessionData(null, null, State.SIGNEDOUT);
    ServerFacade server = new ServerFacade(serverUrl);
    InputHandler input = new InputHandler(server);
    Scanner scanner = new Scanner(System.in);
    System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");

    while (session.state() != State.QUIT) {

      System.out.print(
          (session.username() == null ? "" : "[" + session.username() + "]") +
          (" > "));
      in = scanner.nextLine();
      try {
        switch (session.state()) {

        case SIGNEDOUT:
          response = input.parseSignedOut(session, in);
          break;

        case SIGNEDIN:
          response = input.parseSignedIn(session, in);
          break;

        case INGAME:
          response = input.parseInGame(session, in);
          break;

        case QUIT:
          break;
        }

      } catch (ResponseException exception) {
        response = new HandlerResponse(session, exception.getErrorMessage());
      }
      session = response.data();
      System.out.println(response.output());
    }

    scanner.close();
  }
}
