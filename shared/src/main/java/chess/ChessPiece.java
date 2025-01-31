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
                int[][] king_directions = {
                        {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}, {0, -1}, {0, +1}, {-1, 0}, {+1, 0}
                };
                for (int[] direction : king_directions) {
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];


                    if (inBounds(newRow, newCol)){
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (canMove(newPosition, board)){
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }

                }
                break;

            case QUEEN:
                int[][] queen_directions = {
                        {0, -1}, {0, +1}, {+1, 0}, {-1, 0}, {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}
                };
                for (int[] direction : queen_directions) {
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
                break;

            case BISHOP:
                    int[][] bishop_directions = {
                          {-1, +1}, {-1, -1}, {+1, -1}, {+1, +1}
                    };
                    for (int[] direction : bishop_directions) {
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

                break;
//
            case KNIGHT:
                int[][] knight_directions = {
                        {-1, -2}, {+1, -2}, {+2, -1}, {+2, +1},{-1, +2}, {+1, +2}, {-2, -1}, {-2, +1}
                };
                for (int [] direction : knight_directions){
                    int newRow = row + direction[0];
                    int newCol = col + direction[1];

                    if (inBounds(newRow, newCol)){
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (canMove(newPosition, board)){
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }

                }

                break;

            case ROOK:
                int[][] rook_directions = {
                        {0, -1}, {0, +1}, {+1, 0}, {-1, 0}
                };
                for (int[] direction : rook_directions) {
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

                break;

            case PAWN:
                int[][] pawn_directions;
                int[][] pawn_captures;
                if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
                    pawn_captures = new int[][] {{+1, +1}, {+1, -1}};
                    if (myPosition.getRow() == 2){
                        pawn_directions = new int[][] {{+1, 0}, {+2, 0}};
                    }
                    else {
                        pawn_directions = new int[][] {{+1, 0}};
                    }
                }
                else {
                    pawn_captures = new int[][] {{-1, +1}, {-1, -1}};
                    if (myPosition.getRow() == 7) {
                        pawn_directions = new int[][] {{-1, 0}, {-2, 0}};
                    } else {
                        pawn_directions = new int[][] {{-1, 0}};
                    }
                }
                for (int[] direction : pawn_directions) {
                    int newRow = row;
                    int newCol = col;
                    newRow += direction[0];
                    newCol += direction[1];

                    if (inBounds(newRow, newCol)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (board.getPiece(newPosition) == null) {
                            if ((newRow == 8 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) || (newRow == 1 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK)){
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                            }
                            else {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }

                        }
                        else{
                            break;
                        }
                    }
                }
                for(int[] direction : pawn_captures){
                    int newRow = row;
                    int newCol = col;
                    newRow += direction[0];
                    newCol += direction[1];
                    if (inBounds(newRow, newCol)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        if (board.getPiece(newPosition) != null) {
                            if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                                if ((newRow == 8 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) || (newRow == 1 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK)) {
                                    moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                                    moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                    moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                                    moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                                } else {
                                    moves.add(new ChessMove(myPosition, newPosition, null));
                                }
                            }
                        }
                    }


                }
                break;
        }

        return moves;
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


