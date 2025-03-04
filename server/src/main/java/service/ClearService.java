package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public ClearService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }
}