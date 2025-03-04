package service;

import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.UUID;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Error: Bad request");
        }
        int gameID = Math.abs(UUID.randomUUID().hashCode());
        GameData game = new GameData(gameID, null, null, gameName, null);

        gameDAO.insertGame(game);
        return gameID;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameDAO.listGames();
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        if (playerColor == null) {
            throw new DataAccessException("Error: Bad request - Invalid team color");
        }

        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new DataAccessException("Error: Game not found");
        }
        if (playerColor == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
            throw new DataAccessException("Error: White team already taken");
        }
        if (playerColor == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
            throw new DataAccessException("Error: Black team already taken");
        }

        GameData updatedGame = new GameData(
                game.gameID(),
                playerColor == ChessGame.TeamColor.WHITE ? username : game.whiteUsername(),
                playerColor == ChessGame.TeamColor.BLACK ? username : game.blackUsername(),
                game.gameName(),
                game.game()
        );
        gameDAO.updateGame(updatedGame);

    }
}
