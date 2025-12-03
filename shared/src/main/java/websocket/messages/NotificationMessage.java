package websocket.messages;

import java.util.Objects;

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

  public String getMessage() { return message; }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o))
      return false;
    if (!(o instanceof NotificationMessage that))
      return false;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), message);
  }
}
