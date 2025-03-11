package dataaccess;

import model.GameData;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {
    private SQLGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void testInsertGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(0, "alice", "bob", "Game 1", chessGame);
        gameDAO.insertGame(game);
        GameData retrievedGame = gameDAO.getGame(1);
        assertNotNull(retrievedGame);
        assertEquals("Game 1", retrievedGame.gameName());
    }

    @Test
    public void testUpdateGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(0, "alice", "bob", "Game 1", chessGame);
        gameDAO.insertGame(game);
        GameData updatedGame = new GameData(1, "alice", "carol", "Game 1", chessGame);
        gameDAO.updateGame(updatedGame);
        GameData retrievedGame = gameDAO.getGame(1);
        assertEquals("carol", retrievedGame.blackUsername());
    }

    @Test
    public void testClear() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(0, "alice", "bob", "Game 1", chessGame);
        gameDAO.insertGame(game);
        gameDAO.clear();
        assertNull(gameDAO.getGame(1));
    }
}