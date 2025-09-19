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

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */

    public enum PieceType {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN}

    /**
     * @return Which team this chess piece belongs to
     */

    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */

    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king
     * in danger
     *
     * @return Collection of valid moves
     */

    public enum SpaceStatus {BLOCK, KILL, EMPTY, OOB, KILLOREMPTY}

    public class SpaceChecker {
        private ChessBoard board;
        private HashSet<ChessMove> output;
        private ChessPosition start;

        public SpaceChecker(ChessBoard board, HashSet<ChessMove> output, ChessPosition start) {
            this.board = board;
            this.output = output;
            this.start = start;
        }

        public SpaceStatus CheckAddSpace(int x, int y, SpaceStatus addCondition){
            return CheckAddSpace(x, y, addCondition, false);
        }

        public SpaceStatus CheckAddSpace(int x, int y, SpaceStatus addCondition, boolean promotion) {
            PieceType[] promotionOptions = {PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK};
            ChessPosition targetPosition = new ChessPosition(y + 1, x + 1);

            if (targetPosition.getRow() > 8 || targetPosition.getColumn() > 8 || targetPosition.getColumn() < 1 || targetPosition.getRow() < 1) {
                return SpaceStatus.OOB;
            }

            ChessPiece target = board.getPiece(targetPosition);

            if (target == null) {
                if (addCondition == SpaceStatus.EMPTY || addCondition == SpaceStatus.KILLOREMPTY) {
                    if(!promotion) {
                        output.add(new ChessMove(start, targetPosition, null));
                    }
                    else{
                        for(PieceType option : promotionOptions){
                            output.add(new ChessMove(start, targetPosition, option));
                        }
                    }
                }
                return SpaceStatus.EMPTY;
            }
            if (target.getTeamColor() != pieceColor) {
                if (addCondition == SpaceStatus.KILL || addCondition == SpaceStatus.KILLOREMPTY) {
                    if(!promotion) {
                        output.add(new ChessMove(start, targetPosition, null));
                    }
                    else{
                        for(PieceType option : promotionOptions){
                            output.add(new ChessMove(start, targetPosition, option));
                        }
                    }                }
                return SpaceStatus.KILL;
            } else {
                return SpaceStatus.BLOCK;
            }

        }

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board,
                                            ChessPosition myPosition) {
        System.out.println(myPosition);
        int x = myPosition.getColumn() - 1;
        int y = myPosition.getRow() - 1;
        System.out.println(x + " " + y);

        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        SpaceStatus status;
        SpaceChecker checker = new SpaceChecker(board, validMoves, myPosition);

        switch (type) {
            case PAWN:
                boolean promotion = false;
                if(pieceColor == ChessGame.TeamColor.WHITE) {
                    if(y == 6) {
                        promotion = true;
                    }
                    status = checker.CheckAddSpace(x, y + 1, SpaceStatus.EMPTY, promotion);
                    if (y == 1 && status == SpaceStatus.EMPTY) {
                        //Check if it's the two move starting special
                        checker.CheckAddSpace(x, y + 2, SpaceStatus.EMPTY);
                    }
                    checker.CheckAddSpace(x + 1, y + 1, SpaceStatus.KILL, promotion);
                    checker.CheckAddSpace(x - 1, y + 1, SpaceStatus.KILL, promotion);
                }
                else{
                    if(y == 1) {
                        promotion = true;
                    }
                    status = checker.CheckAddSpace(x, y - 1, SpaceStatus.EMPTY, promotion);
                    if (y == 6 && status == SpaceStatus.EMPTY) {
                        //Check if it's the two move starting special
                        checker.CheckAddSpace(x, y - 2, SpaceStatus.EMPTY);
                    }
                    checker.CheckAddSpace(x + 1, y - 1, SpaceStatus.KILL, promotion);
                    checker.CheckAddSpace(x - 1, y - 1, SpaceStatus.KILL, promotion);
                }

                break;

            case ROOK:
                for (int i = 1; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                break;

            case KNIGHT:
                checker.CheckAddSpace(x + 1, y + 2, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x + 2, y + 1, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x + 2, y - 1, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x + 1, y - 2, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x - 1, y - 2, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x - 2, y - 1, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x - 2, y + 1, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x - 1, y + 2, SpaceStatus.KILLOREMPTY);
                break;

            case BISHOP:
                for (int i = 1; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                break;

            case QUEEN:
                for (int i = 1; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 1; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x + i, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y + i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    status = checker.CheckAddSpace(x - i, y - i, SpaceStatus.KILLOREMPTY);
                    if (status != SpaceStatus.EMPTY) {
                        break;
                    }
                }
                break;

            case KING:
                checker.CheckAddSpace(x + 1, y, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x - 1, y, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x, y + 1, SpaceStatus.KILLOREMPTY);
                checker.CheckAddSpace(x, y - 1, SpaceStatus.KILLOREMPTY);
                break;
        }

        return validMoves;
    }

    @Override
    public String toString() {
        String out = "";
        switch (type) {
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
        if (pieceColor == ChessGame.TeamColor.WHITE) {
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
