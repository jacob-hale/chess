package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.UserData;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        // Validate input
        if (username == null || password == null || email == null) {
            throw new DataAccessException("Error: Bad request");
        }

        // Check if the username is already taken
        if (userDAO.getUser(username) != null) {
            throw new DataAccessException("Error: Username already taken");
        }

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create and insert the user
        UserData user = new UserData(username, hashedPassword, email);
        userDAO.insertUser(user);

        // Generate an auth token and insert it
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.insertAuth(authData);

        return authData;
    }

    public AuthData login(String username, String password) throws DataAccessException {
        // Retrieve the user from the database
        UserData user = userDAO.getUser(username);
        if (user == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        // Verify the password
        if (!BCrypt.checkpw(password, user.password())) {
            throw new DataAccessException("Error: Unauthorized");
        }

        // Generate an auth token and insert it
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.insertAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        // Check if the auth token exists
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        // Delete the auth token
        authDAO.deleteAuth(authToken);
    }
}