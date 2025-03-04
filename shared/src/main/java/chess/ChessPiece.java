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
                int[][] kingDirections = {
                        {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}, {0, -1}, {0, +1}, {-1, 0}, {+1, 0}
                };
                directionsKing(board, myPosition, moves, row, col, kingDirections);
                break;

            case QUEEN:
                int[][] queenDirections = {
                        {0, -1}, {0, +1}, {+1, 0}, {-1, 0}, {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}
                };
                directionsQueen(board, myPosition, moves, row, col, queenDirections);
                break;

            case BISHOP:
                    int[][] bishopDirections = {
                          {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}
                    };
                directionsQueen(board, myPosition, moves, row, col, bishopDirections);

                break;
//
            case KNIGHT:
                int[][] knightDirections = {
                        {-1, -2}, {+1, -2}, {+2, -1}, {+2, +1},{-1, +2}, {+1, +2}, {-2, -1}, {-2, +1}
                };
                directionsKing(board, myPosition, moves, row, col, knightDirections);

                break;

            case ROOK:
                int[][] rookDirections = {
                        {0, -1}, {0, +1}, {+1, 0}, {-1, 0}
                };
                directionsQueen(board, myPosition, moves, row, col, rookDirections);

                break;

            case PAWN:
                int[][] pawnDirections;
                int[][] pawnCaptures;
                if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
                    pawnCaptures = new int[][] {{+1, +1}, {+1, -1}};
                    if (myPosition.getRow() == 2){
                        pawnDirections = new int[][] {{+1, 0}, {+2, 0}};
                    }
                    else {
                        pawnDirections = new int[][] {{+1, 0}};
                    }
                }
                else {
                    pawnCaptures = new int[][] {{-1, +1}, {-1, -1}};
                    if (myPosition.getRow() == 7) {
                        pawnDirections = new int[][] {{-1, 0}, {-2, 0}};
                    } else {
                        pawnDirections = new int[][] {{-1, 0}};
                    }
                }
                for (int[] direction : pawnDirections) {
                    int newRow = row;
                    int newCol = col;
                    newRow += direction[0];
                    newCol += direction[1];

                    if (inBounds(newRow, newCol)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (board.getPiece(newPosition) == null) {
                            directionsPawn(board, myPosition, moves, newRow, newPosition);

                        }
                        else{
                            break;
                        }
                    }
                }
                for(int[] direction : pawnCaptures){
                    int newRow = row;
                    int newCol = col;
                    newRow += direction[0];
                    newCol += direction[1];
                    if (inBounds(newRow, newCol)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (board.getPiece(newPosition) != null) {
                            if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                                directionsPawn(board, myPosition, moves, newRow, newPosition);
                            }
                        }
                    }


                }
                break;
        }

        return moves;
    }

    private void directionsPawn(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int newRow, ChessPosition newPosition) {
        if ((newRow == 8 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE)
                || (newRow == 1 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK)){
            moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        }
        else {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    private void directionsQueen(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int[][] queenDirections) {
        for (int[] direction : queenDirections) {
            int newRow = row;
            int newCol = col;

            while (true) {
                newRow += direction[0];
                newCol += direction[1];

                if (!inBounds(newRow, newCol)) {
                    break;
                }

                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (canMove(newPosition, board)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));

                    if (board.getPiece(newPosition) != null) {
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }
    }

    private void directionsKing(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int[][] kingDirections) {
        for (int[] direction : kingDirections) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];


            if (inBounds(newRow, newCol)){
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (canMove(newPosition, board)){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }
    }

    //    create an is in bounds method that returns true or false if the next move is within the 8x8 board
    public boolean inBounds (int row, int col) {
    return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public boolean canMove(ChessPosition move, ChessBoard board) {
        if (!inBounds(move.getRow(), move.getColumn())) {
            return false;
        }
        ChessPiece target = board.getPiece(move);
        return target == null || target.pieceColor != this.pieceColor;
        }
    }


