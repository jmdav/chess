package server.websocket;

import com.google.gson.Gson;

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
        case MAKE_MOVE -> processMove(action.getAuthToken(), action.getGameID(), action.getMoveData(), ctx.session);
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

  private void join(String authToken, Integer gameID, Session session) throws IOException, DataAccessException {
    connections.add(session);
    connections.send(session, new LoadGameMessage(gameService.getGameById(gameID).game()));
    AuthData sessionData = userService.getSession(authToken);
    TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
    var message = "";
    if (color != null) {
      message = String.format("%s has joined the game as %s.", sessionData.username(),
          color.toString().toLowerCase());
    } else {
      message = String.format("%s has joined the game as an observer.", sessionData.username());
    }
    System.out.println(message);
    var notification = new NotificationMessage(message);
    connections.broadcast(session, notification);
  }

  private void exit(String authToken, Integer gameID, Session session) throws IOException, DataAccessException {
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
  }

  private void processMove(String authToken, Integer gameID, chess.ChessMove moveData, Session session)
      throws IOException, DataAccessException {

    AuthData sessionData = userService.getSession(authToken);
    TeamColor color = gameService.getColorByUsername(gameService.getGameById(gameID), sessionData.username());
    var message = String.format("%s (%s) has made a move: %s",
        sessionData.username(),
        color.toString().toLowerCase(),
        moveData.toString());
  }

}