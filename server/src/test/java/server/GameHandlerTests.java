package server;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTests {
    private GameHandler gameHandler;
    private GameService gameService;
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        gameService = new GameService(new MemoryGameDAO());
        authDAO = new MemoryAuthDAO();
        gameHandler = new GameHandler(gameService, authDAO);
    }

    @Test
    void createGamePositive() throws DataAccessException {
        // Simulate a valid request
        Request req = new TestRequest("{\"gameName\":\"Test Game\"}", "validToken");
        TestResponse res = new TestResponse();

        // Add a valid auth token to the authDAO
        authDAO.insertAuth(new AuthData("validToken", "user1"));

        Object result = gameHandler.createGame(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(200, res.status, "Response status should be 200");
    }

    @Test
    void createGameNegative() throws DataAccessException {
        // Simulate an invalid request (no auth token)
        Request req = new TestRequest("{\"gameName\":\"Test Game\"}", "invalidToken");
        TestResponse res = new TestResponse();

        Object result = gameHandler.createGame(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(401, res.status, "Response status should be 401");
    }

    @Test
    void joinPlayerPositive() throws DataAccessException {
        // Create a game and add it to the gameDAO
        int gameID = gameService.createGame("Test Game");

        // Simulate a valid request
        Request req = new TestRequest("{\"gameID\":" + gameID + ",\"playerColor\":\"WHITE\"}", "validToken");
        TestResponse res = new TestResponse();

        // Add a valid auth token to the authDAO
        authDAO.insertAuth(new AuthData("validToken", "user1"));

        Object result = gameHandler.joinPlayer(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(200, res.status, "Response status should be 200");
    }

    @Test
    void joinPlayerNegative() throws DataAccessException {
        // Simulate an invalid request (no auth token)
        Request req = new TestRequest("{\"gameID\":1,\"playerColor\":\"WHITE\"}", "invalidToken");
        TestResponse res = new TestResponse();

        Object result = gameHandler.joinPlayer(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(401, res.status, "Response status should be 401");
    }

    // Custom TestRequest class to simulate Spark's Request
    private static class TestRequest extends Request {
        private final String body;
        private final String authToken;

        public TestRequest(String body, String authToken) {
            this.body = body;
            this.authToken = authToken;
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public String headers(String header) {
            if ("authorization".equals(header)) {
                return authToken;
            }
            return null;
        }
    }

    // Custom TestResponse class to simulate Spark's Response
    private static class TestResponse extends Response {
        public int status = 200;

        @Override
        public void status(int status) {
            this.status = status;
        }

        @Override
        public int status() {
            return status;
        }
    }
}