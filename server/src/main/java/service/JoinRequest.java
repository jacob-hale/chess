package service;

import chess.ChessGame;

public record JoinRequest(int gameID, ChessGame.TeamColor playerColor) {
    public JoinRequest {
        if (playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK) {
            playerColor = null;
        }
    }
}
