package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDAOTests {
    private MemoryUserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
    }

    @Test
    void clearPositive() throws DataAccessException {
        UserData user = new UserData("user1", "password", "user1@example.com");
        userDAO.insertUser(user);

        userDAO.clear();
        assertNull(userDAO.getUser("user1"), "User should not exist after clearing the database");
    }
}