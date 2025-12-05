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
        case RESIGN -> System.out.println("player resigned...");
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
      connections.add(session);
      connections.send(session, new LoadGameMessage(gameService.getGameById(gameID).game()));
      System.out.println(message);
      var notification = new NotificationMessage(message);
      connections.broadcast(session, notification);
    } catch (IndexOutOfBoundsException e) {
      connections.send(session, new ErrorMessage("Error: Game not found"));
    } catch (DataAccessException e) {
      connections.send(session, new ErrorMessage("Error: Unauthorized"));
    }
  }

  private void exit(String authToken, Integer gameID, Session session) throws IOException {
    try {
      AuthData sessionData = userService.getSession(authToken);
      TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
      var message = "";
      if (color != null) {
        message = String.format("%s (%s) has left the game.", sessionData.username(),
            color.toString().toLowerCase());
      } else {
        message = String.format("%s (observer) has left the game.", sessionData.username());
      }
      var notification = new NotificationMessage(message);
      connections.broadcast(session, notification);
      connections.remove(session);
    } catch (DataAccessException e) {
      connections.send(session, new ErrorMessage("Error: Unauthorized"));
    }
  }

  private void processMove(String authToken, Integer gameID, chess.ChessMove moveData, Session session)
      throws IOException, DataAccessException {

    AuthData sessionData = userService.getSession(authToken);
    ChessGame game = gameService.getGameById(gameID).game();
    TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
    try {
      System.out.println("Processing move: " + moveData.toString());
      game.makeMove(moveData);
    } catch (InvalidMoveException e) {
      connections.send(session, new ErrorMessage("Error: Invalid move"));
      return;
    }
    ServerMessage message = new NotificationMessage(String.format("%s (%s) has made a move: %s",
        sessionData.username(),
        color.toString().toLowerCase(),
        moveData.toString()));

    connections.broadcast(null, new LoadGameMessage(gameService.getGameById(gameID).game()));
    connections.broadcast(null, message);
    String checkMessage = "";

    if (game.isInCheckmate(TeamColor.WHITE)) {
      checkMessage = "White is in checkmate. Black wins!";
    } else if (game.isInCheckmate(TeamColor.BLACK)) {
      checkMessage = "Black is in checkmate. White wins!";
    } else if (game.isInStalemate(TeamColor.WHITE) || game.isInStalemate(TeamColor.BLACK)) {
      checkMessage = "Game is in stalemate. Game is a draw!";
    } else if (game.isInCheck(TeamColor.WHITE)) {
      checkMessage = "White is in check.";
    } else if (game.isInCheck(TeamColor.BLACK)) {
      checkMessage = "Black is in check.";
    }

    if (!checkMessage.isEmpty()) {
      connections.broadcast(null, new NotificationMessage(checkMessage));
    }
  }

}