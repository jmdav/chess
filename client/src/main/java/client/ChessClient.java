package client;

import java.util.Scanner;

public class ChessClient {

  private State state = State.SIGNEDOUT;
  private String username;
  private String out;
  private String in;

  public void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");

    while (state != State.QUIT) {

      System.out.print((username == null ? "" : "[" + username + "]") +
                       (" > "));
      String in = scanner.nextLine();

      switch (state) {

      case SIGNEDOUT:
        out = InputHandler.parseSignedOut(in);
        break;

      case SIGNEDIN:
        out = InputHandler.parseSignedIn(in);
        break;

      case INGAME:
        out = InputHandler.parseInGame(in);
        break;

      case QUIT:
        break;
      }

      System.out.println(out);
    }

    scanner.close();
  }
}
