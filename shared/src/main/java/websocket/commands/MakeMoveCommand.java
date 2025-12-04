package websocket.commands;

import chess.ChessMove;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class MakeMoveCommand extends UserGameCommand {

  public MakeMoveCommand(CommandType commandType, String authToken,
      Integer gameID, ChessMove moveData) {
    super(CommandType.MAKE_MOVE, authToken, gameID, moveData);
  }

}
