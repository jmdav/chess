package client;

public class InputHandler {

  public static ServerFacade server;

  public static String parseSignedOut(String in, SessionData data) {
    String out = "";
    String[] tokens = in.toLowerCase().split(" ");

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Displays list of possible commands \n"
            + "[r]egister <username> <password> <email> - register new user\n"
            + "[l]ogin <username> <password> - login existing user\n"
            + "[q]uit - exit program\n";
      break;

    case "r":
    case "register":
      server.register(data, tokens[1], tokens[2], tokens[3]);
      out = "User " + tokens[1] + " registered successfully.";
      break;

    case "l":
    case "login":
      server.login(data, tokens[1], tokens[2]);
      out = "User " + tokens[1] + " logged in successfully.";
      // somehow set status
      break;

    case "q":
    case "quit":
      // somehow set status
      out = "Quitting program...";
      break;
    }
    return out;
  }

  public static String parseSignedIn(String in, SessionData data) {
    String out = "";
    String[] tokens = in.toLowerCase().split(" ");

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Displays list of possible commands \n"
            + "[r]egister <username> <password> <email> - register new user\n"
            + "[l]ogin <username> <password> - login existing user\n"
            + "[q]uit - exit program\n";
      break;

    case "r":
    case "register":
      server.register(data, tokens[1], tokens[2], tokens[3]);
      out = "User " + tokens[1] + " registered successfully.";
      break;

    case "l":
    case "login":
      server.login(data, tokens[1], tokens[2]);
      out = "User " + tokens[1] + " logged in successfully.";
      // somehow set status
      break;

    case "q":
    case "quit":
      // somehow set status
      out = "Quitting program...";
      break;
    }
    return out;
  }
  public static String parseInGame(String in) { return "Not yet implemented."; }
}