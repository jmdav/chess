package websocket.messages;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class LoadGameMessage extends ServerMessage {

  ServerMessageType serverMessageType;

  public LoadGameMessage() {
    super(ServerMessageType.LOAD_GAME);
  }

}
