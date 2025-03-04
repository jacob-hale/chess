package chess;

import java.util.ArrayList;
import java.util.Collection;
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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

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
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        switch (getPieceType()) {
            case KING:
                calculateKingMoves(board, myPosition, moves, row, col);
                break;
            case QUEEN:
                calculateQueenMoves(board, myPosition, moves, row, col);
                break;
            case BISHOP:
                calculateBishopMoves(board, myPosition, moves, row, col);
                break;
            case KNIGHT:
                calculateKnightMoves(board, myPosition, moves, row, col);
                break;
            case ROOK:
                calculateRookMoves(board, myPosition, moves, row, col);
                break;
            case PAWN:
                calculatePawnMoves(board, myPosition, moves, row, col);
                break;
        }
        return moves;
    }

    private void calculateKingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] kingDirections = {
                {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}, {0, -1},
                {0, +1}, {-1, 0}, {+1, 0}
        };
        directionsKing(board, myPosition, moves, row, col, kingDirections);
    }

    private void calculateQueenMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] queenDirections = {
                {0, -1}, {0, +1}, {+1, 0}, {-1, 0}, {-1, +1},
                {-1, -1}, {+1, -1}, {+1, +1}
        };
        directionsQueen(board, myPosition, moves, row, col, queenDirections);
    }

    private void calculateBishopMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] bishopDirections = {
                {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}
        };
        directionsQueen(board, myPosition, moves, row, col, bishopDirections);
    }

    private void calculateKnightMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] knightDirections = {
                {-1, -2}, {+1, -2}, {+2, -1}, {+2, +1},
                {-1, +2}, {+1, +2}, {-2, -1}, {-2, +1}
        };
        directionsKing(board, myPosition, moves, row, col, knightDirections);
    }

    private void calculateRookMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] rookDirections = {
                {0, -1}, {0, +1}, {+1, 0}, {-1, 0}
        };
        directionsQueen(board, myPosition, moves, row, col, rookDirections);
    }

    private void calculatePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col) {
        int[][] pawnDirections;
        int[][] pawnCaptures;

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            pawnCaptures = new int[][]{{+1, +1}, {+1, -1}};
            pawnDirections = (row == 2) ? new int[][]{{+1, 0}, {+2, 0}} : new int[][]{{+1, 0}};
        } else {
            pawnCaptures = new int[][]{{-1, +1}, {-1, -1}};
            pawnDirections = (row == 7) ? new int[][]{{-1, 0}, {-2, 0}} : new int[][]{{-1, 0}};
        }

        calculatePawnDirectionMoves(board, myPosition, moves, row, col, pawnDirections);
        calculatePawnCaptureMoves(board, myPosition, moves, row, col, pawnCaptures);
    }

    private void calculatePawnDirectionMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                                             int row, int col, int[][] directions) {
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (!inBounds(newRow, newCol)) {
                continue;
            }

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) == null) {
                addPawnMove(board, myPosition, moves, newRow, newPosition);
            } else {
                break; // Stop if the path is blocked
            }
        }
    }

    private void calculatePawnCaptureMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                                           int row, int col, int[][] directions) {
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (!inBounds(newRow, newCol)) {
                continue;
            }

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece target = board.getPiece(newPosition);
            if (target != null && target.getTeamColor() != pieceColor) {
                addPawnMove(board, myPosition, moves, newRow, newPosition);
            }
        }
    }

    private void addPawnMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                             int newRow, ChessPosition newPosition) {
        if ((newRow == 8 && pieceColor == ChessGame.TeamColor.WHITE) ||
                (newRow == 1 && pieceColor == ChessGame.TeamColor.BLACK)) {
            moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    private void directionsQueen(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                                 int row, int col, int[][] directions) {
        for (int[] direction : directions) {
            int newRow = row;
            int newCol = col;

            while (true) {
                newRow += direction[0];
                newCol += direction[1];

                if (!inBounds(newRow, newCol)) {
                    break;
                }

                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (!canMove(newPosition, board)) {
                    break;
                }

                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }
    }

    private void directionsKing(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                                int row, int col, int[][] directions) {
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (inBounds(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (canMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean canMove(ChessPosition move, ChessBoard board) {
        ChessPiece target = board.getPiece(move);
        return target == null || target.getTeamColor() != pieceColor;
    }
}