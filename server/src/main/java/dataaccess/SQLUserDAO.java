package dataaccess;

import model.UserData;
import java.sql.*;

public class SQLUserDAO implements UserDAO {
    @Override
    public void insertUser(UserData user) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email()); // Add this line
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email") // Use null as default if email is not required
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get user: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear users: " + e.getMessage());
        }
    }
}