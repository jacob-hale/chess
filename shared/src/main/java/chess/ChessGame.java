package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTurn = TeamColor.WHITE;
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
//        if spot is empty
        if(piece == null){
            return null;
        }
//        get the piece's possible moves
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

//        for each possible move make the move on a temporary board and see if it puts its team in check
        for(ChessMove move:possibleMoves){
            ChessBoard tempBoard = new ChessBoard(board);
            ChessPiece movingPiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());

            tempBoard.addPiece(move.getEndPosition(), movingPiece);
            tempBoard.addPiece(move.getStartPosition(), null);

            ChessBoard originalBoard = board;
            board = tempBoard;
            boolean isValidMove = !isInCheck(piece.getTeamColor());
            board = originalBoard;
//            if move doesn't put team in check, add it to the validMoves collection
            if (isValidMove) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
//        no piece in spot
        if (piece == null) {
            throw new InvalidMoveException("Invalid move: No piece at the start position.");
        }
//        not team's turn
        if (piece.getTeamColor() != currentTurn) {
            throw new InvalidMoveException("Invalid move: It's not your turn.");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
//        checks to see if the move appears in validMoves. If not, throw exception
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: The move is not legal.");
        }
//        handles promotion for pawns
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), piece);
        }
//        gets rid of piece in the start position
        board.addPiece(move.getStartPosition(), null);
//        switches turns
        currentTurn = (currentTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null) {
            return false;
        }
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(currentPosition);

                if (piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(board, currentPosition);

                    for (ChessMove move : possibleMoves){
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Finds the king's position for a certain team
     * @param teamColor which team's king you want to find
     * @return teamColor's king position
     */
    private ChessPosition findKingPosition(TeamColor teamColor){
//        goes through each square and when the piece is a king of the wanted teamColor, return the position
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col ++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null &&
                    piece.getPieceType() == ChessPiece.PieceType.KING &&
                    piece.getTeamColor() == teamColor) {

                    return position;
                }
            }
        }
    return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
//        goes through each square and checks all pieces of teamColor, if validMoves is empty, teamColor is in checkmate
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()){
                        return false;
                    }

                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
//        goes through each square. If every piece of teamColor can't move, they are in stalemate
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
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
        return this.board;
    }
}
