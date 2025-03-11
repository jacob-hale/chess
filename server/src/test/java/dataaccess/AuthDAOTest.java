package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {
    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void testInsertAuth() throws DataAccessException {
        AuthData auth = new AuthData("alice", "token123");
        authDAO.insertAuth(auth);
        AuthData retrievedAuth = authDAO.getAuth("token123");
        assertNotNull(retrievedAuth);
        assertEquals("alice", retrievedAuth.username());
    }

    @Test
    public void testGetAuthNotFound() throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth("nonexistent");
        assertNull(retrievedAuth);
    }

    @Test
    public void testDeleteAuth() throws DataAccessException {
        AuthData auth = new AuthData("alice", "token123");
        authDAO.insertAuth(auth);
        authDAO.deleteAuth("token123");
        assertNull(authDAO.getAuth("token123"));
    }

    @Test
    public void testClear() throws DataAccessException {
        AuthData auth = new AuthData("alice", "token123");
        authDAO.insertAuth(auth);
        authDAO.clear();
        assertNull(authDAO.getAuth("token123"));
    }
}