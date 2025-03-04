package server;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import spark.Response;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTests {
    @Test
    void handleDataAccessExceptionPositive() {
        TestResponse res = new TestResponse();

        DataAccessException exception = new DataAccessException("Test error");

        String response = ErrorHandler.handleDataAccessException(exception, res);

        assertTrue(response.contains("Test error"), "Response should contain the error message");

        assertEquals(500, res.status, "Response status should be 500");
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