package service;

import chess.ChessGame;
import dataaccess.DataAccessException;


public record JoinRequest(int gameID, ChessGame.TeamColor playerColor) {
}
