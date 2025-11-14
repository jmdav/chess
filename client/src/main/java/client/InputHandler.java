package client;

import client.errors.ResponseException;
import java.util.List;
import model.GameData;

public class InputHandler {

  ServerFacade server;
  String out = "";
  List<GameData> games;

  public InputHandler(ServerFacade server) { this.server = server; }

  public HandlerResponse parseSignedOut(SessionData data, String in)
      throws ResponseException {

    String[] tokens = tokenize(in);
    out = "";
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
      data = server.register(data, tokens[1], tokens[2], tokens[3]);
      out = "User " + data.username() + " registered successfully.";
      break;

    case "l":
    case "login":
      if (tokens.length < 3)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <username> <password>");
      data = server.login(data, tokens[1], tokens[2]);
      out = "User " + data.username() + " logged in successfully.";
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
    return new HandlerResponse(data, out);
  }

  public HandlerResponse parseSignedIn(SessionData data, String in)
      throws ResponseException {
    out = "";
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
      data = new SessionData(null, null, State.SIGNEDOUT);
      break;

    case "c":
    case "creategame":
      if (tokens.length < 2)
        throw new ResponseException(
            "Error: insufficient arguments. Expected <gameName>");
      GameData newGame = server.createGame(data, tokens[1]);
      out = "Game " + newGame.gameName() + " created successfully.";
      break;

    case "g":
    case "listgames":
      games = server.listGames(data);
      GameData g;
      for (int i = 0; i < games.size(); i++) {
        g = games.get(i);
        System.out.println(i + ") " + g.gameName() +
                           " // Black: " + g.blackUsername() +
                           " | White: " + g.whiteUsername());
      }
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
    return new HandlerResponse(data, out);
  }

  public HandlerResponse parseInGame(SessionData data, String in)
      throws ResponseException {
    return new HandlerResponse(data, out);
  }

  private String[] tokenize(String in) {
    String[] out = in.toLowerCase().split(" ");
    out[0] = out[0].toLowerCase();
    return out;
  }
}