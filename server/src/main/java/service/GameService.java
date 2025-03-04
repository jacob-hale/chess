package service;

import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Map;
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
}
