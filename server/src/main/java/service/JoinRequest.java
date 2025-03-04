package service;

import chess.ChessGame;
import dataaccess.DataAccessException;


public record JoinRequest(int gameID, ChessGame.TeamColor playerColor) {
//    public JoinRequest {
//        if (playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK) {
//            throw new DataAccessException("Error: Bad request - Invalid team color");
//        }
//    }
}
