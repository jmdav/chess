package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {

  private ChessGame.TeamColor pieceColor;
  private PieceType type;

  public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
      this.pieceColor = pieceColor;
      this.type = type;
  }

  /**
   * The various different chess piece options
   */

  public enum PieceType { KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }

  /**
   * @return Which team this chess piece belongs to
   */

  public ChessGame.TeamColor getTeamColor() { return pieceColor; }

  /**
   * @return which type of chess piece this piece is
   */

  public PieceType getPieceType() { return type; }

  /**
   * Calculates all the positions a chess piece can move to
   * Does not take into account moves that are illegal due to leaving the king
   * in danger
   *
   * @return Collection of valid moves
   */

  public Collection<ChessMove> pieceMoves(ChessBoard board,
                                          ChessPosition myPosition) {
      int x = myPosition.getColumn();
      int y = myPosition.getRow();

    return new HashSet<ChessMove>();
  }

    @Override
    public String toString() {
        String out = "";
        switch(type){
            case KING -> {
                out = "k";
            }
            case QUEEN -> {
                out = "q";
            }
            case KNIGHT -> {
                out = "n";
            }
            case ROOK -> {
                out = "r";
            }
            case BISHOP -> {
                out = "b";
            }
            case PAWN -> {
                out = "p";
            }
        }
        if(pieceColor == ChessGame.TeamColor.WHITE){
            out = out.toUpperCase(Locale.ROOT);
        }
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
