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

        switch(getPieceType()) {
            case KING:
                if(inBounds(row -1, col)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col), null));
                }
                if(inBounds(row -1, col +1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col+1), null));
                }
                if(inBounds(row, col+1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col+1), null));
                }
                if(inBounds(row +1, col+1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col+1), null));
                }
                if(inBounds(row +1, col)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col), null));
                }
                if(inBounds(row +1, col-1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col-1), null));
                }
                if(inBounds(row, col-1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col-1), null));
                }
                if(inBounds(row -1, col-1)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row-1, col-1), null));
                }
                break;

//            case QUEEN:
//
//                break;
//
            case BISHOP:
                    int[][] directions = {
//                      go southwest
                        {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7},
//                      go southeast
                        {-1, +1}, {-2, +2}, {-3, +3}, {-4, +4}, {-5, +5}, {-6, +6}, {-7, +7},
//                      go northwest
                        {+1, -1}, {+2, -2}, {+3, -3}, {+4, -4}, {+5, -5}, {+6, -6}, {+7, -7},
//                      go northeast
                        {+1, +1}, {+2, +2}, {+3, +3}, {+4, +4}, {+5, +5}, {+6, +6}, {+7, +7}
                    };
                    for (int[] direction : directions) {
                        int newRow = row + direction[0];
                        int newCol = col + direction[1];

                        if (inBounds(newRow, newCol)) {
                            ChessPosition newPosition = new ChessPosition(newRow, newCol);
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }

                    }
                break;
//
//            case KNIGHT:
//
//                break;
//
//            case ROOK:
//
//                break;
//
//            case PAWN:
//
//                break;
        }
        for (ChessMove move : moves) {
            ChessPosition startPosition = move.getStartPosition();
            ChessPosition endPosition = move.getEndPosition();
            System.out.println("Move from: (" + startPosition.getRow() + ", " + startPosition.getColumn() + ") " +
                    "to: (" + endPosition.getRow() + ", " + endPosition.getColumn() + ")");
        }
        return moves;
    }
//    create an is in bounds method that returns true or false if the next move is within the 8x8 board
    public boolean inBounds (int row, int col) {
    return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

}
