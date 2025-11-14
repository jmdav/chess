package client;

public class InputHandler {
  public static String parseSignedOut(String in) {
    String out;
    String[] tokens = in.toLowerCase().split(" ");

    switch (tokens[0]) {
    case "?":
    case "h":
    case "help":
      out = "no.";
    case "r":
    case "register":
      server.register(tokens[1], tokens[2], tokens[3]);
    }
  }
  public static String parseSignedIn(String in) {
    String[] tokens = in.toLowerCase().split(" ");
  }
  public static String parseInGame(String in) {
    String[] tokens = in.toLowerCase().split(" ");
  }
}