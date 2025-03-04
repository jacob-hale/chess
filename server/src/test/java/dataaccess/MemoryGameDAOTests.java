package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemoryGameDAOTests {
    private MemoryGameDAO gameDAO;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
    }

    @Test
    void updateGamePositive() throws DataAccessException {
        GameData game = new GameData(1, null, null, "Test Game", null);
        gameDAO.insertGame(game);

        GameData updatedGame = new GameData(1, "user1", "user2", "Test Game", null);
        gameDAO.updateGame(updatedGame);

        GameData retrievedGame = gameDAO.getGame(1);
        assertEquals("user1", retrievedGame.whiteUsername(), "White username should be updated");
        assertEquals("user2", retrievedGame.blackUsername(), "Black username should be updated");
    }

    @Test
    void updateGameNegative() {
        GameData game = new GameData(1, null, null, "Test Game", null);
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(game),
                "Updating a non-existent game should throw an exception");
    }

    @Test
    void clearPositive() throws DataAccessException {
        GameData game = new GameData(1, null, null, "Test Game", null);
        gameDAO.insertGame(game);

        gameDAO.clear();
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(1),
                "Game should not exist after clearing the database");
    }
}