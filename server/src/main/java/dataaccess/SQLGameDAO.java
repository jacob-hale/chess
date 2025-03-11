package dataaccess;

import model.GameData;
import chess.ChessGame;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        String sql = "INSERT INTO games (game_name, white_username, black_username, game_state) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, game.gameName());
            preparedStatement.setString(2, game.whiteUsername());
            preparedStatement.setString(3, game.blackUsername());
            preparedStatement.setString(4, gson.toJson(game.game())); // Serialize ChessGame to JSON
            preparedStatement.executeUpdate();

            // Retrieve the auto-generated game ID
            try (var rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    int gameID = rs.getInt(1);
                    game = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert game: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM games WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String gameStateJson = rs.getString("game_state");
                    ChessGame game = gson.fromJson(gameStateJson, ChessGame.class); // Deserialize JSON to ChessGame
                    return new GameData(
                            rs.getInt("id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("game_name"),
                            game
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get game: " + e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String sql = "SELECT * FROM games";
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String gameStateJson = rs.getString("game_state");
                    ChessGame game = gson.fromJson(gameStateJson, ChessGame.class); // Deserialize JSON to ChessGame
                    games.add(new GameData(
                            rs.getInt("id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("game_name"),
                            game
                    ));
                }
                return games;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to list games: " + e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String sql = "UPDATE games SET white_username = ?, black_username = ?, game_state = ? WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, gson.toJson(game.game())); // Serialize ChessGame to JSON
            preparedStatement.setInt(4, game.gameID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update game: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear games: " + e.getMessage());
        }
    }
}