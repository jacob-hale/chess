package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    private UserService userService;
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void registerPositive() throws DataAccessException {
        AuthData authData = userService.register("user1", "password", "user1@example.com");
        assertNotNull(authData, "AuthData should not be null");
        assertEquals("user1", authData.username(), "Username should match");
        assertNotNull(authData.authToken(), "Auth token should not be null");
    }

    @Test
    void registerNegative() {
        assertThrows(DataAccessException.class, () -> userService.register(null, "password", "user1@example.com"),
                "Registering with a null username should throw an exception");
        assertThrows(DataAccessException.class, () -> userService.register("user1", null, "user1@example.com"),
                "Registering with a null password should throw an exception");
        assertThrows(DataAccessException.class, () -> userService.register("user1", "password", null),
                "Registering with a null email should throw an exception");
    }

    @Test
    void loginPositive() throws DataAccessException {
        userService.register("user1", "password", "user1@example.com");
        AuthData authData = userService.login("user1", "password");
        assertNotNull(authData, "AuthData should not be null");
        assertEquals("user1", authData.username(), "Username should match");
        assertNotNull(authData.authToken(), "Auth token should not be null");
    }

    @Test
    void loginNegative() throws DataAccessException {
        userService.register("user1", "password", "user1@example.com");
        assertThrows(DataAccessException.class, () -> userService.login("user1", "wrongpassword"),
                "Logging in with an incorrect password should throw an exception");
        assertThrows(DataAccessException.class, () -> userService.login("nonexistent", "password"),
                "Logging in with a nonexistent username should throw an exception");
    }

    @Test
    void logoutPositive() throws DataAccessException {
        AuthData authData = userService.register("user1", "password", "user1@example.com");
        userService.logout(authData.authToken());
        assertNull(authDAO.getAuth(authData.authToken()), "Auth token should be deleted after logout");
    }

    @Test
    void logoutNegative() {
        assertThrows(DataAccessException.class, () -> userService.logout("invalidToken"),
                "Logging out with an invalid auth token should throw an exception");
    }
}