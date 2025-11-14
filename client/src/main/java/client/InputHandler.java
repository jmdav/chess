package client;

public class InputHandler {

  public static ServerFacade server;

  public static String parseSignedOut(SessionData data, String in) {
    String out = "";
    String[] tokens = in.toLowerCase().split(" ");

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Display list of possible commands \n"
            + "[r]egister <username> <password> <email> - Register new user\n"
            + "[l]ogin <username> <password> - Login existing user\n"
            + "[q]uit - Quit program\n";
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

  public static String parseSignedIn(SessionData data, String in) {
    String out = "";
    String[] tokens = in.toLowerCase().split(" ");

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Displays list of possible commands \n"
            + "[l]ogout - Log out current user\n"
            + "[c]reategame <name> - Create new chess game\n"
            + "list[g]ames - Get a list of all current games\n"
            + "[j]oingame <number> <color> - Join game by listed number\n"
            + "[o]bservegame <number> - Observe game by listed number\n";
      break;

    case "l":
    case "logout":
      server.logout(data);
      out = "Logged out successfully.";
      break;

    case "c":
    case "creategame":
      server.login(data, tokens[1], tokens[2]);
      out = "User " + tokens[1] + " logged in successfully.";
      // somehow set status
      break;

    case "g":
    case "listgames":
      out = server.listGames(data);
      break;

    case "j":
    case "joingame":
      // somehow set status
      out = server.joinGame(data, tokens[1], tokens[2]);
      break;

    case "o":
    case "observegame":
      // somehow set status
      out = server.observeGame(data, tokens[1]);
      break;
    }
    return out;
  }
  public static String parseInGame(SessionData data, String in) {
    return "Not yet implemented.";
  }
}