package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            throw new DataAccessException("Error: Game ID already exists");
        }
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Error: Game not found");
        }
        return game;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Error: Game not found");
        }
        games.put(game.gameID(), game);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
