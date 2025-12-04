package websocket.messages;

import com.google.gson.Gson;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class NotificationMessage extends ServerMessage {
  private final String message;

  public NotificationMessage(String message) {
    super(ServerMessageType.NOTIFICATION);
    this.message = message;
  }

  public String toString() {
    return new Gson().toJson(this);
  }

  public String getMessage() {
    return this.message;
  }

}
