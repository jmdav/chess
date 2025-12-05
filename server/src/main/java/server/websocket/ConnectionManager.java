package server.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

public class ConnectionManager {

  public final ConcurrentHashMap<Session, Integer> connections = new ConcurrentHashMap<>();

  public void add(Session session, Integer gameID) {
    connections.put(session, gameID);
  }

  public void remove(Session session) {
    connections.remove(session);
  }

  public void send(Session session, Integer gameID, ServerMessage message)
      throws IOException {
    System.out.println("ConnectionManager.send called - session: " + session + ", isOpen: "
        + (session != null ? session.isOpen() : "null"));
    if (session != null && session.isOpen()) {
      String msg = message.toString();
      System.out.println("Sending message: " + msg);
      session.getRemote().sendString(msg);
      System.out.println("Message sent successfully");
    } else {
      System.out.println("Cannot send - session is " + (session == null ? "null" : "closed"));
    }
  }

  public void broadcast(Session excludeSession, Integer gameID, ServerMessage notification)
      throws IOException {
    String msg = notification.toString();
    for (Session c : connections.keySet()) {
      if (c.isOpen() && connections.get(c).equals(gameID)) {
        if (!c.equals(excludeSession)) {
          c.getRemote().sendString(msg);
        }
      }
    }
  }
}