package websocket.commands;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */

public class ConnectCommand extends UserGameCommand {

  public ConnectCommand(CommandType commandType, String authToken,
      Integer gameID) {
    super(CommandType.CONNECT, authToken, gameID);
  }

}
