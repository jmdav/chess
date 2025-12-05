package server.websocket;

import com.google.gson.Gson;

import chess.ChessGame;
import chess.InvalidMoveException;
import chess.ChessGame.TeamColor;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameRequestData;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import service.GameService;
import service.UserService;

public class WebSocketHandler
    implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

  private final GameService gameService;
  private final UserService userService;

  public WebSocketHandler(GameService gameService, UserService userService) {
    this.gameService = gameService;
    this.userService = userService;
  }

  private final ConnectionManager connections = new ConnectionManager();

  @Override
  public void handleConnect(WsConnectContext ctx) {
    System.out.println("Websocket connected");
    ctx.enableAutomaticPings();
  }

  @Override
  public void handleMessage(WsMessageContext ctx) {
    System.out.println("Websocket message received");
    try {
      UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
      switch (action.getCommandType()) {
        case CONNECT -> join(action.getAuthToken(), action.getGameID(), ctx.session);
        case MAKE_MOVE -> processMove(action.getAuthToken(), action.getGameID(), action.getMove(), ctx.session);
        case RESIGN -> resign(action.getAuthToken(), action.getGameID(), ctx.session);
        case LEAVE -> exit(action.getAuthToken(), action.getGameID(), ctx.session);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void handleClose(WsCloseContext ctx) {
    System.out.println("Websocket closed");
  }

  private void join(String authToken, Integer gameID, Session session) throws IOException {
    try {
      AuthData sessionData = userService.getSession(authToken);
      TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
      var message = "";
      if (color != null) {
        message = String.format("%s has joined the game as %s.", sessionData.username(),
            color.toString().toLowerCase());
      } else {
        message = String.format("%s has joined the game as an observer.", sessionData.username());
      }
      connections.add(session, gameID);
      connections.send(session, gameID, new LoadGameMessage(gameService.getGameById(gameID).game()));
      System.out.println("Game loaded for joining player." + gameService.getGameById(gameID).game().toString());
      System.out.println(message);
      var notification = new NotificationMessage(message);
      connections.broadcast(session, gameID, notification);
    } catch (IndexOutOfBoundsException e) {
      connections.send(session, gameID, new ErrorMessage("Error: Game not found"));
    } catch (DataAccessException e) {
      connections.send(session, gameID, new ErrorMessage("Error: Unauthorized"));
    }
  }

  private void exit(String authToken, Integer gameID, Session session) throws IOException {
    try {
      AuthData sessionData = userService.getSession(authToken);
      TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
      var message = "";
      if (color != null) {
        gameService.leaveGame(authToken, new GameRequestData(color, gameID));
        message = String.format("%s (%s) has left the game.", sessionData.username(),
            color.toString().toLowerCase());
      } else {
        message = String.format("%s (observer) has left the game.", sessionData.username());
      }
      var notification = new NotificationMessage(message);
      connections.broadcast(session, gameID, notification);
      connections.remove(session);
    } catch (DataAccessException e) {
      connections.send(session, gameID, new ErrorMessage("Error: Unauthorized"));
    }
  }

  private void resign(String authToken, Integer gameID, Session session) throws IOException {
    try {
      ChessGame game = gameService.getGameById(gameID).game();
      AuthData sessionData = userService.getSession(authToken);
      TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
      var message = "";
      if (game.isActiveGame() == false) {
        connections.send(session, gameID, new ErrorMessage("Error: Game has already ended."));
        return;
      }
      if (color != null) {
        message = String.format("%s (%s) has resigned.", sessionData.username(),
            color.toString().toLowerCase());
        game.endGame();
      } else {
        connections.send(session, gameID, new ErrorMessage("Error: Observers cannot resign. Try [L]eave"));
        return;
      }
      var notification = new NotificationMessage(message);
      gameService.updateGameData(gameID, game);
      connections.broadcast(null, gameID, notification);
      // connections.broadcast(null, new
      // LoadGameMessage(gameService.getGameById(gameID).game()));

    } catch (DataAccessException e) {
      connections.send(session, gameID, new ErrorMessage("Error: Unauthorized"));
    }
  }

  private void processMove(String authToken, Integer gameID, chess.ChessMove moveData, Session session)
      throws IOException {

    try {
      AuthData sessionData = userService.getSession(authToken);
      ChessGame game = gameService.getGameById(gameID).game();
      TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
      if (color != game.getTeamTurn()) {
        connections.send(session, gameID, new ErrorMessage("Error: Not active player"));
        return;
      }
      if (game.isActiveGame() == false) {
        connections.send(session, gameID, new ErrorMessage("Error: Game has ended"));
        return;
      }
      try {
        System.out.println("Pre move: " + game.getTeamTurn());
        game.makeMove(moveData);
        System.out.println("Post move: " + game.getTeamTurn());
      } catch (InvalidMoveException e) {
        connections.send(session, gameID, new ErrorMessage("Error: Invalid move"));
        return;
      }
      System.out.println("Pre announce: " + game.getTeamTurn());
      ServerMessage message = new NotificationMessage(String.format("%s (%s) has made a move: %s",
          sessionData.username(),
          color.toString().toLowerCase(),
          moveData.toString()));

      String checkMessage = "";
      System.out.println("Pre checkmate: " + game.getTeamTurn());
      if (game.isInCheckmate(TeamColor.WHITE)) {
        checkMessage = "White is in checkmate. Black wins!";
        game.endGame();
      } else if (game.isInCheckmate(TeamColor.BLACK)) {
        checkMessage = "Black is in checkmate. White wins!";
        game.endGame();
      } else if (game.isInStalemate(TeamColor.WHITE) || game.isInStalemate(TeamColor.BLACK)) {
        checkMessage = "Game is in stalemate. Game is a draw!";
        game.endGame();
      } else if (game.isInCheck(TeamColor.WHITE)) {
        checkMessage = "White is in check.";
      } else if (game.isInCheck(TeamColor.BLACK)) {
        checkMessage = "Black is in check.";
      }
      System.out.println("Pre update: " + game.getTeamTurn());
      gameService.updateGameData(gameID, game);
      System.out.println("Post update: " + game.getTeamTurn());
      connections.broadcast(null, gameID, new LoadGameMessage(game));
      connections.broadcast(session, gameID, message);

      if (!checkMessage.isEmpty()) {
        connections.broadcast(null, gameID, new NotificationMessage(checkMessage));
      }

    } catch (DataAccessException e) {
      connections.send(session, gameID, new ErrorMessage("Error:" + e.getMessage()));
    }
  }

}