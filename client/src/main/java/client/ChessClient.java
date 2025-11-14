package client;

import java.util.Scanner;

public class ChessClient {

  private State state = State.SIGNEDOUT;
  private String username;
  private String out;
  private String in;

  public void run() {
    SessionData session = new SessionData();
    Scanner scanner = new Scanner(System.in);
    System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");

    while (state != State.QUIT) {

      System.out.print((username == null ? "" : "[" + username + "]") +
                       (" > "));
      in = scanner.nextLine();

      switch (state) {

      case SIGNEDOUT:
        out = InputHandler.parseSignedOut(session, in);
        break;

      case SIGNEDIN:
        out = InputHandler.parseSignedIn(session, in);
        break;

      case INGAME:
        out = InputHandler.parseInGame(session, in);
        break;

      case QUIT:
        break;
      }

      System.out.println(out);
    }

    scanner.close();
  }
}
