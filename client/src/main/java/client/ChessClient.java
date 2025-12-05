package client;

import client.errors.ResponseException;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import websocket.messages.NotificationMessage;

import java.util.Scanner;

public class ChessClient implements NotificationHandler {

  private HandlerResponse response;
  private String in;
  SessionData session;

  public void run(String serverUrl) throws ResponseException {
    session = new SessionData(null, null, State.SIGNEDOUT);
    ServerFacade server = new ServerFacade(serverUrl);
    WebSocketFacade socket = new WebSocketFacade(serverUrl, this);
    InputHandler input = new InputHandler(server, socket);
    Scanner scanner = new Scanner(System.in);
    System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");

    while (session.state() != State.QUIT) {

      printCaret();
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

  public void notify(NotificationMessage notification) {
    System.out.println(notification.getMessage());
    printCaret();
  }

  public void printCaret() {
    System.out.print(
        (session.username() == null ? "" : "[" + session.username() + "]") +
            (" > "));
  }

}
