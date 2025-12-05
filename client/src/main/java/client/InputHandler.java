package client;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import client.errors.ResponseException;
import client.websocket.WebSocketFacade;

import java.util.List;
import java.util.Scanner;

import model.GameData;
import ui.BoardRender;

public class InputHandler {

  ServerFacade server;
  WebSocketFacade socket;
  String out = "";
  List<GameData> games;
  ChessGame game;
  TeamColor color;
  Integer gameID;

  public InputHandler(ServerFacade server, WebSocketFacade socket) {
    this.server = server;
    this.socket = socket;
  }

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
        if (tokens.length < 4) {
          throw new ResponseException("Error: insufficient arguments. Expected "
              + "<username> <password> <email>");
        }
        data = server.register(data, tokens[1], tokens[2], tokens[3]);
        out = "User " + data.username() + " registered successfully.";
        break;

      case "l":
      case "login":
        if (tokens.length < 3) {
          throw new ResponseException(
              "Error: insufficient arguments. Expected <username> <password>");
        }
        data = server.login(data, tokens[1], tokens[2]);
        out = "User " + data.username() + " logged in successfully.";
        break;

      case "q":
      case "quit":
        out = "Quitting program...";
        data = new SessionData(null, null, State.QUIT);
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
        if (tokens.length < 2) {
          throw new ResponseException(
              "Error: insufficient arguments. Expected <gameName>");
        }
        server.createGame(data, tokens[1]);
        out = "Game " + tokens[1] + " created successfully.";
        break;

      case "g":
      case "listgames":
        games = server.listGames(data);
        GameData g;
        for (int i = 0; i < games.size(); i++) {
          g = games.get(i);
          System.out.println((i + 1) + ") " + g.gameName() +
              " // Black: " + g.blackUsername() +
              " | White: " + g.whiteUsername());
        }
        break;

      case "j":
      case "joingame":
        // somehow set status
        if (tokens.length < 3) {
          throw new ResponseException("Error: insufficient arguments. Expected "
              + "<gameID> <color (W or B)>");
        }
        ChessGame.TeamColor color;
        if (games == null) {
          games = server.listGames(data);
        }
        Integer gameID = listToGameID(tokens[1]);
        switch (tokens[2].toLowerCase()) {
          case "w":
            color = ChessGame.TeamColor.WHITE;
            break;
          case "b":
            color = ChessGame.TeamColor.BLACK;
            break;
          default:
            throw new ResponseException(
                "Error: invalid color. Expected <gameID> <color (W or B)>");
        }
        server.joinGame(data, gameID, color);
        socket.join(data.authToken(), gameID);
        out = "Game " + tokens[1] + " joined as " + color;
        this.color = color;
        data = new SessionData(data.authToken(), data.username(), State.INGAME);
        break;

      case "o":
      case "observegame":
        if (tokens.length < 2) {
          throw new ResponseException(
              "Error: insufficient arguments. Expected <gameID>");
        }
        Integer observeGameID = listToGameID(tokens[1]);
        out = server.observeGame(data, observeGameID);
        socket.join(data.authToken(), observeGameID);
        this.color = TeamColor.WHITE;
        data = new SessionData(data.authToken(), data.username(), State.INGAME);
        break;

      default:
        out = "Error: Invalid command. Type 'Help' for a list of commands.";
    }
    return new HandlerResponse(data, out);
  }

  public HandlerResponse parseInGame(SessionData data, String in)
      throws ResponseException {
    out = "";
    String[] tokens = tokenize(in);

    switch (tokens[0]) {

      case "?":
      case "h":
      case "help":
        out = "[h]elp - Displays list of possible commands \n"
            + "[d]raw - Draw chess board\n"
            + "[l]eave - Leave game\n"
            + "[m]ove <start position> <end position> - Make move\n"
            + "[r]esign - Accept defeat\n"
            + "h[i]ghlight - Highlight all legal moves";
        break;

      case "d":
      case "draw":
        BoardRender.render(game.getBoard(), color);
        break;

      case "l":
      case "leave":
        socket.leave(data.authToken(), gameID);
        out = "Left game.";
        data = new SessionData(data.authToken(), data.username(), State.SIGNEDIN);
        break;

      case "m":
      case "move":
        if (tokens.length < 3) {
          throw new ResponseException(
              "Error: insufficient arguments. Expected <start position> <end position> <piece promotion (Q, R, B, N)(if applicable)>");
        }
        chess.ChessMove move;
        if (tokens.length == 4) {
          try {
            move = new chess.ChessMove(parsePosition(tokens[1]), parsePosition(tokens[2]),
                parsePiece(tokens[3]));
          } catch (ResponseException e) {
            throw new ResponseException("Error: invalid piece promotion. Expected (Q, R, B, N)");
          }
        } else {
          move = new chess.ChessMove(parsePosition(tokens[1]), parsePosition(tokens[2]), null);
        }
        socket.make_move(data.authToken(), gameID, move);
        out = "Move made: " + move.toString();
        break;

      case "r":
      case "resign":
        System.out.println("Are you sure? y/n");
        Scanner scanner = new Scanner(System.in);
        in = scanner.nextLine();
        if (!in.trim().toLowerCase().equals("y")) {
          out = "Resignation cancelled.";
          break;
        }
        socket.resign(data.authToken(), gameID);
        out = "You have resigned from the game.";
        data = new SessionData(data.authToken(), data.username(), State.SIGNEDIN);
        break;

      case "i":
      case "highlight":
        if (tokens.length < 3) {
          throw new ResponseException(
              "Error: insufficient arguments. Expected <piece position>)");
        }
        chess.ChessPosition position = parsePosition(tokens[1]);
        BoardRender.render(game.getBoard(), color,
            game.getValidMoves(position), position);
        break;
      default:
        out = "Error: Invalid command. Type 'Help' for a list of commands.";
    }
    return new HandlerResponse(data, out);
  }

  private String[] tokenize(String in) {
    String[] out = in.toLowerCase().split(" ");
    out[0] = out[0].toLowerCase();
    return out;
  }

  private chess.ChessPiece.PieceType parsePiece(String piece)
      throws ResponseException {
    piece = piece.trim().toLowerCase();
    switch (piece) {
      case "q":
      case "queen":
        return chess.ChessPiece.PieceType.QUEEN;
      case "r":
      case "rook":
        return chess.ChessPiece.PieceType.ROOK;
      case "b":
      case "bishop":
        return chess.ChessPiece.PieceType.BISHOP;
      case "n":
      case "knight":
        return chess.ChessPiece.PieceType.KNIGHT;
      default:
        throw new ResponseException(
            "Error: invalid piece type. Expected <piece promotion (Q, R, B, N)>");
    }
  }

  private chess.ChessPosition parsePosition(String pos) throws ResponseException {
    pos = pos.trim().toLowerCase();
    if (pos.length() != 2) {
      throw new ResponseException(
          "Error: invalid position. Expected <column><row> (e.g. d4)");
    }
    int col = Character.getNumericValue(pos.charAt(0)) - 9;
    int row = Character.getNumericValue(pos.charAt(1));

    return new chess.ChessPosition(col, row);
  }

  private Integer listToGameID(String in) throws ResponseException {
    Integer gameID;
    try {
      gameID = Integer.parseInt(in);
    } catch (NumberFormatException e) {
      throw new ResponseException(
          "Error: invalid game ID. Expected <gameID> <color (W or B)>");
    }
    try {
      gameID = games.get(gameID - 1).gameID();
    } catch (IndexOutOfBoundsException e) {
      throw new ResponseException("Error: Game does not exist.");
    }
    return gameID;
  }

  void updateGame(ChessGame game) {
    this.game = game;
    BoardRender.render(game.getBoard(), color);
  }
}
