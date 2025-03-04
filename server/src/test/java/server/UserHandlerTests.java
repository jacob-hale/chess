package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.*;

class UserHandlerTests {
    private UserHandler userHandler;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        userHandler = new UserHandler(userService);
    }

    @Test
    void registerPositive() {
        // Simulate a valid request
        Request req = new TestRequest("{\"username\":\"user1\",\"password\":\"password\",\"email\":\"user1@example.com\"}", null);
        TestResponse res = new TestResponse();

        Object result = userHandler.register(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(200, res.status, "Response status should be 200");
    }

    @Test
    void registerNegative() {
        // Simulate an invalid request (missing username)
        Request req = new TestRequest("{\"username\":null,\"password\":\"password\",\"email\":\"user1@example.com\"}", null);
        TestResponse res = new TestResponse();

        Object result = userHandler.register(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(400, res.status, "Response status should be 400");
    }

    @Test
    void loginPositive() throws DataAccessException {
        // Register a user first
        userService.register(new RegisterRequest("user1", "password", "user1@example.com"));

        // Simulate a valid request
        Request req = new TestRequest("{\"username\":\"user1\",\"password\":\"password\"}", null);
        TestResponse res = new TestResponse();

        Object result = userHandler.login(req, res);
        assertNotNull(result, "Result should not be null");
        assertEquals(200, res.status, "Response status should be 200");
    }

    @Test
    void loginNegative() throws DataAccessException {
        // Register a user first
        userService.register(new RegisterRequest("user1", "password", "user1@example.com"));

        // Simulate an invalid request (wrong password)
        Request req = new TestRequest("{\"username\":\"user1\",\"password\":\"wrongpassword\"}", null);
        TestResponse res = new TestResponse();

        Object result = userHandler.login(req, res);
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