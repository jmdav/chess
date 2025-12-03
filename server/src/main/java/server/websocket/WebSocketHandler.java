package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import java.io.IOException;
import javax.management.Notification;
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
    try {
      UserGameCommand action =
          new Gson().fromJson(ctx.message(), UserGameCommand.class);
      switch (action.getCommandType()) {
                  case CONNECT -> join(action.visitorName(), ctx.session);
                  case MAKE_MOVE -> exit(action.visitorName(), ctx.session);
                  case RESIGN -> System.out.println("player resigned...");
                  case LEAVE -> System.out.println("player left...");
              }
          } catch (IOException ex) {
              ex.printStackTrace();
          }
    }

    @
    Override public void handleClose(WsCloseContext ctx) {
      System.out.println("Websocket closed");
    }

    private void join(String userName, Session session) throws IOException {
      connections.add(session);
      var message = String.format("%s has joined the game as %s", userName);
      var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcast(session, notification);
    }

    private void exit(String visitorName, Session session) throws IOException {
      var message = String.format("%s left the shop", visitorName);
      var notification = new ServerMessage(ServerMessage.ServerMessageType.DEPARTURE, message);
      connections.broadcast(session, notification);
      connections.remove(session);
    }

    public void makeNoise(String petName, String sound)
        throws ResponseException {
      try {
        var message = String.format("%s says %s", petName, sound);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOISE, message);
        connections.broadcast(null, notification);
      } catch (Exception ex) {
        throw new ResponseException(ResponseException.Code.ServerError,
                                    ex.getMessage());
      }
    }
  }