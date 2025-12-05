package websocket.messages;

import com.google.gson.Gson;

import chess.ChessGame;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class LoadGameMessage extends ServerMessage {

  public LoadGameMessage(ChessGame game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public String toString() {
    return new Gson().toJson(this);
  }

  public ChessGame getGame() {
    return this.game;
  }

}
