package websocket.messages;

import com.google.gson.Gson;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class ErrorMessage extends ServerMessage {
  private final String errorMessage;

  public ErrorMessage(String message) {
    super(ServerMessageType.ERROR);
    this.errorMessage = message;
  }

  public String toString() {
    return new Gson().toJson(this);
  }

  public String getErrorMessage() {
    return this.errorMessage;
  }

}
