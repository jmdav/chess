package ui;

import static ui.EscapeSequences.*;

import chess.ChessBoard;
import chess.ChessGame.TeamColor;
import chess.ChessPiece;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BoardRender {

  // Board dimensions.
  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static ChessPiece[][] pieces;
  private static TeamColor bgcolor;

  public static void render(ChessBoard board, TeamColor color) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    pieces = board.getBoard();
    out.print(ERASE_SCREEN);

    drawHeaders(out, color);
    drawChessBoard(out, color);
    drawHeaders(out, color);
    resetText(out);
  }

  private static void drawHeaders(PrintStream out, TeamColor color) {

    String[] headers = {"   ", " A ", " B ", " C ", " D ",
                        " E ", " F ", " G ", " H ", "   "};
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES + 2; ++boardCol) {
      if (color == TeamColor.WHITE) {
        drawHeader(out, headers[boardCol]);
      } else {
        drawHeader(out, headers[9 - boardCol]);
      }
    }

    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {

    int prefixLength = 1 / 2;
    int suffixLength = 1 - prefixLength - 1;
    setContextColors(out);
    out.print(EMPTY.repeat(prefixLength));
    out.print(headerText);
    out.print(EMPTY.repeat(suffixLength));
  }

  private static void drawChessBoard(PrintStream out, TeamColor color) {
    bgcolor = TeamColor.BLACK;
    int start;
    int end;
    int step;
    if (color == TeamColor.BLACK) {
      start = 0;
      end = BOARD_SIZE_IN_SQUARES;
      step = 1;
    } else {
      start = BOARD_SIZE_IN_SQUARES - 1;
      end = -1;
      step = -1;
    }

    for (int i = start; i != end; i += step) {

      drawColumn(out, pieces, i, color);

      if (i < BOARD_SIZE_IN_SQUARES - 1) {
        // Draw horizontal row separator.
        setBlack(out);
      }
    }
  }

  private static void drawColumn(PrintStream out, ChessPiece[][] pieces,
                                 int col, TeamColor color) {
    setContextColors(out);
    out.print(" " + (col + 1) + " ");
    alternateColor(out);

    int colstart;
    int colend;
    int colstep;
    if (color == TeamColor.WHITE) {
      colstart = 0;
      colend = BOARD_SIZE_IN_SQUARES;
      colstep = 1;
    } else {
      colstart = BOARD_SIZE_IN_SQUARES - 1;
      colend = -1;
      colstep = -1;
    }

    for (int i = colstart; i != colend; i += colstep) {
      alternateColor(out);
      ChessPiece piece = pieces[i][col];

      if (piece == null) {
        out.print(EMPTY);
      } else {
        TeamColor team = piece.getTeamColor();
        if (team == TeamColor.WHITE) {
          setWhite(out);
        } else {
          setBlack(out);
        }
        switch (piece.getPieceType()) {
        case PAWN:
          out.print(team == TeamColor.BLACK ? BLACK_PAWN : WHITE_PAWN);
          break;
        case ROOK:
          out.print(team == TeamColor.BLACK ? BLACK_ROOK : WHITE_ROOK);
          break;
        case BISHOP:
          out.print(team == TeamColor.BLACK ? BLACK_BISHOP : WHITE_BISHOP);
          break;
        case KNIGHT:
          out.print(team == TeamColor.BLACK ? BLACK_KNIGHT : WHITE_KNIGHT);
          break;
        case QUEEN:
          out.print(team == TeamColor.BLACK ? BLACK_QUEEN : WHITE_QUEEN);
          break;
        case KING:
          out.print(team == TeamColor.BLACK ? BLACK_KING : WHITE_KING);
          break;
        default:
          out.print(EMPTY);
        }
        setBlack(out);
      }
    }
    setContextColors(out);
    out.print(" " + (col + 1) + " ");
    out.println();
  }

  private static void setWhiteBG(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
  }

  private static void setBlackBG(PrintStream out) {
    out.print(SET_BG_COLOR_DARK_GREY);
  }

  private static void resetText(PrintStream out) {
    out.print(RESET_BG_COLOR);
    out.print(RESET_TEXT_COLOR);
  }

  private static void setBlack(PrintStream out) {
    out.print(SET_TEXT_COLOR_BLACK);
  }

  private static void setWhite(PrintStream out) {
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void setContextColors(PrintStream out) {
    out.print(SET_BG_COLOR_DARK_GREY);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void alternateColor(PrintStream out) {
    if (bgcolor == TeamColor.WHITE) {
      setWhiteBG(out);
      bgcolor = TeamColor.BLACK;
    } else {
      setBlackBG(out);
      bgcolor = TeamColor.WHITE;
    }
  }
}