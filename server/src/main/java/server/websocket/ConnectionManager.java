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
    if (session.isOpen()) {
      String msg = message.toString();
      session.getRemote().sendString(msg);
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