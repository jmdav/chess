package chess;

import java.util.Arrays;

import static chess.ChessPiece.PieceType;
import static chess.ChessGame.TeamColor;



/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessBoard {

  private ChessPiece[][] boardArray;

  public ChessBoard() { boardArray = new ChessPiece[8][8]; }

  /**
   * Adds a chess piece to the chessboard
   *
   * @param position where to add the piece to
   * @param piece    the piece to add
   */

  public void addPiece(ChessPosition position, ChessPiece piece) {
      boardArray[position.getColumn()-1][position.getRow()-1] = piece;
  }

  /**
   * Gets a chess piece on the chessboard
   *
   * @param position The position to get the piece from
   * @return Either the piece at the position, or null if no piece is at that
   * position
   */

  public ChessPiece getPiece(ChessPosition position) {
    return boardArray[position.getColumn()-1][position.getRow()-1];
  }

  /**
   * Sets the board to the default starting board
   * (How the game of chess normally starts)
   */
  private void placeComplexRow(int index, ChessGame.TeamColor color){
      boardArray[0][index] = new ChessPiece(color, PieceType.ROOK);
      boardArray[1][index] = new ChessPiece(color, PieceType.KNIGHT);
      boardArray[2][index] = new ChessPiece(color, PieceType.BISHOP);
      if(color == TeamColor.WHITE){
          boardArray[3][index] = new ChessPiece(color, PieceType.QUEEN);
          boardArray[4][index] = new ChessPiece(color, PieceType.KING);
      }
      else{
          boardArray[3][index] = new ChessPiece(color, PieceType.KING);
          boardArray[4][index] = new ChessPiece(color, PieceType.QUEEN);
      }
      boardArray[5][index] = new ChessPiece(color, PieceType.BISHOP);
      boardArray[6][index] = new ChessPiece(color, PieceType.KNIGHT);
      boardArray[7][index] = new ChessPiece(color, PieceType.ROOK);
  }

    private void placePawns(int index, ChessGame.TeamColor color){
        for(int i = 0; i < 8; i++){
            boardArray[i][index] = new ChessPiece(color, PieceType.PAWN);
        }

    }

  public void resetBoard() {
      //Place bottom row
      placeComplexRow(0,TeamColor.WHITE);
      placePawns(1,TeamColor.WHITE);
      placePawns(6,TeamColor.BLACK);
      placeComplexRow(7,TeamColor.BLACK);
  }

    @Override
    public String toString() {
      String out = "";
        for (ChessPiece[] row : boardArray){
            out += Arrays.toString(row);
        }
        return out;
    }

}
