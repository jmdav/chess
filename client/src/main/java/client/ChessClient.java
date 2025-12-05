package client;

import chess.ChessGame;
import client.errors.ResponseException;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import java.util.Scanner;
import websocket.messages.*;

public class ChessClient implements ServerMessageHandler {

  private HandlerResponse response;
  private String in;
  SessionData session;
  InputHandler input;

  public void run(String serverUrl) throws ResponseException {
    session = new SessionData(null, null, State.SIGNEDOUT);
    ServerFacade server = new ServerFacade(serverUrl);
    WebSocketFacade socket = new WebSocketFacade(serverUrl, this);
    input = new InputHandler(server, socket);
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

  @Override
  public void handleMessage(ServerMessage notification) {
    switch (notification.getServerMessageType()) {
    case ERROR:
      System.out.println("Error from server: " +
                         ((ErrorMessage)notification).getErrorMessage());
      printCaret();
      break;
    case NOTIFICATION:
      System.out.println("Notification from server: " +
                         ((NotificationMessage)notification).getMessage());
      printCaret();
      break;
    case LOAD_GAME:
      input.updateGame(((LoadGameMessage)notification).getGame());
    }
  }

  public void printCaret() {
    System.out.print(
        (session.username() == null ? "" : "[" + session.username() + "]") +
        (" > "));
  }

  public void updateGame(ChessGame game) { input.updateGame(game); }
}
