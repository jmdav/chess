package client;

import client.errors.ResponseException;

public class InputHandler {

  ServerFacade server;

  public InputHandler(ServerFacade server) { this.server = server; }

  public String parseSignedOut(SessionData data, String in)
      throws ResponseException {
    String out = "";
    String[] tokens = tokenize(in);

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Display list of possible commands \n"
            + "[r]egister <username> <password> <email> - Register new user\n"
            + "[l]ogin <username> <password> - Login existing user\n"
            + "[q]uit - Quit program";
      break;

    case "r":
    case "register":
      if (tokens.length < 4)
        throw new ResponseException("Error: insufficient arguments. Expected "
                                    + "<username> <password> <email>");
      server.register(data, tokens[1], tokens[2], tokens[3]);
      out = "User " + tokens[1] + " registered successfully.";
      break;

    case "l":
    case "login":
      if (tokens.length < 3)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <username> <password>");
      server.login(data, tokens[1], tokens[2]);
      out = "User " + tokens[1] + " logged in successfully.";
      // somehow set status
      break;

    case "q":
    case "quit":
      // somehow set status
      out = "Quitting program...";
      break;

    default:
      out = "Error: Invalid command. Type 'Help' for a list of commands.";
    }
    return out;
  }

  public String parseSignedIn(SessionData data, String in)
      throws ResponseException {
    String out = "";
    String[] tokens = tokenize(in);

    switch (tokens[0]) {

    case "?":
    case "h":
    case "help":
      out = "[h]elp - Displays list of possible commands \n"
            + "[l]ogout - Log out current user\n"
            + "[c]reategame <name> - Create new chess game\n"
            + "list[g]ames - Get a list of all current games\n"
            + "[j]oingame <number> <color> - Join game by listed number\n"
            + "[o]bservegame <number> - Observe game by listed number";
      break;

    case "l":
    case "logout":
      server.logout(data);
      out = "Logged out successfully.";
      break;

    case "c":
    case "creategame":
      if (tokens.length < 3)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <gameName>");
      server.createGame(data, tokens[1]);
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
      if (tokens.length < 3)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <gameID> <color>");
      out = server.joinGame(data, tokens[1], tokens[2]);
      break;

    case "o":
    case "observegame":
      if (tokens.length < 2)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <gameID>");
      out = server.observeGame(data, tokens[1]);
      // somehow set status
      break;

    default:
      out = "Error: Invalid command. Type 'Help' for a list of commands.";
    }
    return out;
  }

  public String parseInGame(SessionData data, String in)
      throws ResponseException {
    return "Not yet implemented.";
  }

  private String[] tokenize(String in) {
    String[] out = in.toLowerCase().split(" ");
    out[0] = out[0].toLowerCase();
    return out;
  }
}