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
  boolean activeGame = true;

  public ChessGame() {
    teamTurn = TeamColor.WHITE;
    board = new ChessBoard();
    board.resetBoard();
  }

  public Collection<ChessMove> getValidMoves(ChessPosition position) {
    return validMoves(position);
  }

  /**
   * @return Which team's turn it is
   */

  public TeamColor getTeamTurn() {
    return teamTurn;
  }

  public boolean isActiveGame() {
    return activeGame;
  }

  public boolean endGame() {
    activeGame = false;
    return activeGame;
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

  public enum TeamColor {
    WHITE, BLACK
  }

  /**
   * Gets a valid moves for a piece at the given location
   *
   * @param startPosition the piece to get valid moves for
   * @return Set of valid moves for requested piece, or null if no piece at
   *         startPosition
   */

  public Collection<ChessMove> validMoves(ChessPosition startPosition) {

    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    ChessPiece targetPiece = board.getPiece(startPosition);
    TeamColor targetColor = board.getPiece(startPosition).getTeamColor();
    Collection<ChessMove> possibleMoves = targetPiece.pieceMoves(board, startPosition);
    // System.out.println("Before elimination: " + possibleMoves);
    for (ChessMove move : possibleMoves) {
      ChessBoard hypothetical = new ChessBoard(board);
      if (!doesMovePlaceInCheck(targetColor, move, hypothetical)) {
        moves.add(move);
      }
    }
    // System.out.println(moves);
    return moves;
  }

  /**
   * Makes a move in a chess game
   *
   * @param move chess move to perform
   * @throws InvalidMoveException if move is invalid
   */

  public boolean doesMovePlaceInCheck(TeamColor teamColor, ChessMove move, ChessBoard targetBoard) {
    ChessPiece piece = targetBoard.popPiece(move.getStartPosition());
    targetBoard.addPiece(move.getEndPosition(), piece);
    return isInCheck(teamColor, targetBoard);
  }

  public void makeMove(ChessMove move) throws InvalidMoveException {
    ChessPiece piece = board.getPiece(move.getStartPosition());
    if (piece == null || piece.getTeamColor() != teamTurn) {
      throw new InvalidMoveException();
    }
    Collection<ChessMove> validOptions = validMoves(move.getStartPosition());
    for (ChessMove option : validOptions) {
      // System.out.println(option + " " + move);
      if (option.equals(move)) {
        // System.out.println("move made.");
        piece = board.popPiece(move.getStartPosition());

        if (move.getPromotionPiece() != null) {
          piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        board.addPiece(move.getEndPosition(), piece);
        this.teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        System.out.println(teamTurn);
        return;
      }
    }
    throw new InvalidMoveException();
  }

  /**
   * Determines if the given team is in check
   *
   * @param teamColor which team to check for check
   * @return True if the specified team is in check
   */

  public boolean isInCheck(TeamColor teamColor) {
    return isInCheck(teamColor, board);
  }

  public boolean isInCheck(TeamColor teamColor, ChessBoard targetBoard) {

    Collection<ChessPosition> allDangerSpaces = new HashSet<ChessPosition>();
    ChessPosition kingPos = null;

    for (int row = 0; row < targetBoard.length(); row++) {
      for (int col = 0; col < targetBoard.length(); col++) {
        ChessPiece piece = targetBoard.getPiece(new ChessPosition(row + 1, col + 1));
        if (piece == null) {
          continue;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
          if (piece.getTeamColor() == teamColor) {
            kingPos = new ChessPosition(row + 1, col + 1);
          }
        }
        if (piece.getTeamColor() != teamColor) {
          Collection<ChessMove> dangerSpaces = piece.pieceMoves(
              targetBoard, new ChessPosition(row + 1, col + 1), true);
          for (ChessMove move : dangerSpaces) {
            allDangerSpaces.add(move.getEndPosition());
          }
        }
      }
    }
    // System.out.println(allDangerSpaces);
    // System.out.println(kingPos);
    for (ChessPosition dangerSpace : allDangerSpaces) {
      if (dangerSpace.equals(kingPos)) {
        // System.out.println("that's not good!");
        // System.out.println(targetBoard);
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
    if (isInAStickySituation(teamColor) && isInCheck(teamColor)) {
      return true;
    }
    return false;
  }

  private boolean isInAStickySituation(TeamColor teamColor) {
    for (int row = 0; row < board.length(); row++) {
      for (int col = 0; col < board.length(); col++) {
        ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
        if (piece == null) {
          continue;
        }
        if (piece.getTeamColor() == teamColor) {
          if (validMoves(new ChessPosition(row + 1, col + 1)).size() != 0) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Determines if the given team is in stalemate, which here is defined as
   * having no valid moves while not in check.
   *
   * @param teamColor which team to check for stalemate
   * @return True if the specified team is in stalemate, otherwise false
   */
  public boolean isInStalemate(TeamColor teamColor) {
    if (isInAStickySituation(teamColor) && !isInCheck(teamColor)) {
      return true;
    }
    return false;
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
    return teamTurn == chessGame.teamTurn &&
        Objects.equals(board, chessGame.board);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamTurn, board);
  }

  @Override
  public String toString() {
    return "ChessGame{" +
        "teamTurn=" + teamTurn +
        ", board=\n" + board.toString();
  }
}
