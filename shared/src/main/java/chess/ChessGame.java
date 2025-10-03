package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessGame {

    TeamColor teamTurn;
    ChessBoard board;

  public ChessGame() {
      teamTurn = TeamColor.WHITE;
      board = new ChessBoard();
      board.resetBoard();
  }

  /**
   * @return Which team's turn it is
   */
  public TeamColor getTeamTurn() {
      return teamTurn;
  }

  /**
   * Set's which teams turn it is
   *
   * @param team the team whose turn it is
   */
  public void setTeamTurn(TeamColor team) {
    teamTurn = team;
  }

  /**
   * Enum identifying the 2 possible teams in a chess game
   */

  public enum TeamColor { WHITE, BLACK }

  /**
   * Gets a valid moves for a piece at the given location
   *
   * @param startPosition the piece to get valid moves for
   * @return Set of valid moves for requested piece, or null if no piece at
   * startPosition
   */

  public Collection<ChessMove> validMoves(ChessPosition startPosition) {

      HashSet<ChessMove> moves = new HashSet<ChessMove>();
      ChessPiece targetPiece = board.getPiece(startPosition);
      if(targetPiece == null){
          return null;
      }
      Collection<ChessMove> possibleMoves = targetPiece.pieceMoves(board,startPosition);
      for(ChessMove move : possibleMoves) {
          ChessBoard hypothetical = new ChessBoard(board);
          if(!doesMovePlaceInCheck(move,hypothetical)){
            moves.add(move);
          }
      }
      return moves;
  }

  /**
   * Makes a move in a chess game
   *
   * @param move chess move to perform
   * @throws InvalidMoveException if move is invalid
   */

  public boolean doesMovePlaceInCheck(ChessMove move, ChessBoard targetBoard){
      ChessPiece piece = board.popPiece(move.getStartPosition());
      board.addPiece(move.getEndPosition(), piece);
      return isInCheck(teamTurn, targetBoard);
  }

  public void makeMove(ChessMove move) throws InvalidMoveException {
      ChessPiece piece = board.popPiece(move.getStartPosition());
      board.addPiece(move.getEndPosition(), piece);
  }



  /**
   * Determines if the given team is in check
   *
   * @param teamColor which team to check for check
   * @return True if the specified team is in check
   */

  public boolean isInCheck(TeamColor teamColor){
      return isInCheck(teamColor, board);
  }
  public boolean isInCheck(TeamColor teamColor, ChessBoard targetBoard) {

      Collection<ChessPosition> allDangerSpaces = new HashSet<ChessPosition>();
      ChessPosition kingPos = null;

      for (int row = 0; row < targetBoard.length(); row++) {
          for (int col = 0; col < targetBoard.length(); col++) {
              ChessPiece piece = targetBoard.getPiece(new ChessPosition(row,col));
              if (piece == null) break;
              Collection<ChessMove> dangerSpaces = piece.pieceMoves(targetBoard, new ChessPosition(row, col), true);
              for (ChessMove move : dangerSpaces) {
                  allDangerSpaces.add(move.getEndPosition());
              }
              if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                  if (piece.getTeamColor() == teamColor) {
                      kingPos = new ChessPosition(row, col);
                  }
              }
          }
      }
      for(ChessPosition dangerSpace : allDangerSpaces){
          if (dangerSpace == kingPos){
              return true;
          }
      }
      return false;
  }

  /**
   * Determines if the given team is in checkmate
   *
   * @param teamColor which team to check for checkmate
   * @return True if the specified team is in checkmate
   */
  public boolean isInCheckmate(TeamColor teamColor) {
    throw new RuntimeException("Not implemented");
  }

  /**
   * Determines if the given team is in stalemate, which here is defined as
   * having no valid moves while not in check.
   *
   * @param teamColor which team to check for stalemate
   * @return True if the specified team is in stalemate, otherwise false
   */
  public boolean isInStalemate(TeamColor teamColor) {
    throw new RuntimeException("Not implemented");
  }

  /**
   * Sets this game's chessboard with a given board
   *
   * @param board the new board to use
   */
  public void setBoard(ChessBoard board) {
    this.board = board;
  }

  /**
   * Gets the current chessboard
   *
   * @return the chessboard
   */
  public ChessBoard getBoard() {
    return board;
  }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
