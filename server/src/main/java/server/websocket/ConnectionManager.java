package server.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

public class ConnectionManager {
  public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

  public void add(Session session) {
    connections.put(session, session);
  }

  public void remove(Session session) {
    connections.remove(session);
  }

  public void send(Session session, ServerMessage message)
      throws IOException {
    if (session.isOpen()) {
      String msg = message.toString();
      session.getRemote().sendString(msg);
    }
  }

  public void broadcast(Session excludeSession, ServerMessage notification)
      throws IOException {
    String msg = notification.toString();
    for (Session c : connections.values()) {
      if (c.isOpen()) {
        if (!c.equals(excludeSession)) {
          c.getRemote().sendString(msg);
        }
      }
    }
  }
}