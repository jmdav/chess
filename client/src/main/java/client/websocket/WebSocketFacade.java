package client.websocket;

import chess.ChessGame;
import client.errors.ResponseException;
import com.google.gson.Gson;
import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import websocket.commands.*;
import websocket.messages.*;

// need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

  Session session;
  ServerMessageHandler notificationHandler;

  public WebSocketFacade(String url, ServerMessageHandler notificationHandler)
      throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");
      this.notificationHandler = notificationHandler;

      // System.out.println("Connecting to WebSocket at: " + socketURI);
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);
      // System.out.println("WebSocket connected, adding message handler...");

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          try {
            ServerMessage servermsg =
                new Gson().fromJson(message, ServerMessage.class);
            switch (servermsg.getServerMessageType()) {
                            case LOAD_GAME -> load_game(servermsg.getGame());
                            case ERROR -> notificationHandler.handleMessage(new Gson().fromJson(message, ErrorMessage.class));
                            case NOTIFICATION -> notificationHandler.handleMessage(new Gson().fromJson(message, NotificationMessage.class));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    // Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void load_game(ChessGame game) throws ResponseException {
        notificationHandler.updateGame(game);
    }

    public void join(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void make_move(String authToken, Integer gameID, chess.ChessMove move) throws ResponseException {
        try {
            System.out.println(move);
            var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void printCaret(){
        notificationHandler.printCaret();
    }

}