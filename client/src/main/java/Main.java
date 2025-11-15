import chess.ChessBoard;
import chess.ChessGame.TeamColor;
import client.ChessClient;
import ui.BoardRender;

public class Main {
  public static void main(String[] args) {
    ChessBoard board = new ChessBoard();
    board.resetBoard();
    BoardRender.render(board, TeamColor.BLACK);
    // String serverUrl = "http://localhost:8080";
    // if (args.length == 1) {
    //   serverUrl = args[0];
    // }
    // System.out.println(serverUrl);
    // try {
    //   new ChessClient().run(serverUrl);
    // } catch (Throwable ex) {
    //   System.out.println("Unable to connect to server");
    //   System.out.println(ex.getMessage());
    // }
  }
}