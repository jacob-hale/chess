package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameService gameService;
    private MemoryGameDAO gameDAO;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(gameDAO);
    }

    @Test
    void createGamePositive() throws DataAccessException {
        int gameID = gameService.createGame("Test Game");
        assertTrue(gameID > 0, "Game ID should be positive");
        GameData game = gameDAO.getGame(gameID);
        assertNotNull(game, "Game should exist in the database");
        assertEquals("Test Game", game.gameName(), "Game name should match");
    }

    @Test
    void createGameNegative() {
        assertThrows(DataAccessException.class, () -> gameService.createGame(null),
                "Creating a game with a null name should throw an exception");
        assertThrows(DataAccessException.class, () -> gameService.createGame(""),
                "Creating a game with an empty name should throw an exception");
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        gameService.createGame("Game 1");
        gameService.createGame("Game 2");
        Collection<GameData> games = gameService.listGames();
        assertEquals(2, games.size(), "There should be 2 games in the list");
    }

    @Test
    void joinPlayerPositive() throws DataAccessException {
        int gameID = gameService.createGame("Test Game");
        gameService.joinPlayer(gameID, ChessGame.TeamColor.WHITE, "user1");
        GameData game = gameDAO.getGame(gameID);
        assertEquals("user1", game.whiteUsername(), "User should be assigned to the white team");
    }

    @Test
    void joinPlayerNegativeInvalidColor() throws DataAccessException {
        int gameID = gameService.createGame("Test Game");
        assertThrows(DataAccessException.class, () -> gameService.joinPlayer(gameID, null, "user1"),
                "Joining with a null team color should throw an exception");
    }

    @Test
    void joinPlayerNegativeTeamFull() throws DataAccessException {
        int gameID = gameService.createGame("Test Game");
        gameService.joinPlayer(gameID, ChessGame.TeamColor.WHITE, "user1");
        assertThrows(DataAccessException.class, () -> gameService.joinPlayer(gameID, ChessGame.TeamColor.WHITE, "user2"),
                "Joining a full team should throw an exception");
    }
}

