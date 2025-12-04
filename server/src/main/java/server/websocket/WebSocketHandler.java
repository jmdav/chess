package server.websocket;

import com.google.gson.Gson;

import handlers.GameHandler;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

public class WebSocketHandler
    implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

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
        case CONNECT -> join(action.getAuthToken(), ctx.session);
        case MAKE_MOVE -> System.out.println("player made a move...");
        case RESIGN -> System.out.println("player resigned...");
        case LEAVE -> exit(action.getAuthToken(), ctx.session);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void handleClose(WsCloseContext ctx) {
    System.out.println("Websocket closed");
  }

  private void join(String authToken, Session session) throws IOException {
    connections.add(session);
    var message = String.format("%s has joined the game as %s", authToken, authToken);
    System.out.println(message);
    var notification = new NotificationMessage(message);
    connections.broadcast(null, notification);
  }

  private void exit(String authToken, Session session) throws IOException {
    var message = String.format("%s left the game", authToken);
    var notification = new NotificationMessage(message);
    connections.broadcast(null, notification);
    connections.remove(session);
  }

}