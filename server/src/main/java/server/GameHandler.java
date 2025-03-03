package server;

import com.google.gson.Gson;
import model.GameData;
import service.GameService;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.JoinRequest;
import spark.Request;
import spark.Response;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import java.util.Collection;


public class GameHandler {
    private final GameService gameService;
    private final AuthDAO authDAO;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService, AuthDAO authDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
    }
    public Object createGame(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("Error: Unauthorized");
            }
            CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

            int gameID = gameService.createGame(createGameRequest.gameName());

            res.status(200);
            return gson.toJson(new CreateGameResult(gameID));
        } catch (DataAccessException e) {
            return ErrorHandler.handleDataAccessException(e, res);
        }
    }

    public Object listGames(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("Error: Unauthorized");
            }
            Collection<GameData> games = gameService.listGames();
            res.status(200);
            return gson.toJson(new ListGamesResult(games));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("Unauthorized")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    public Object joinPlayer(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            if (authToken == null || authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("Error: Unauthorized");
            }

            JoinRequest joinRequest = gson.fromJson(req.body(), JoinRequest.class);
            if (joinRequest.playerColor() == null) {
                res.status(400);
                return gson.toJson(new ErrorResponse("Error: Bad request - Invalid team color"));
            }
            String username = authDAO.getAuth(authToken).username();


            gameService.joinPlayer(joinRequest.gameID(), joinRequest.playerColor(), username);

            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (e.getMessage().contains("Unauthorized")) {
                res.status(401);
            } else if (e.getMessage().contains("already taken")) {
                res.status(403);
            } else if (e.getMessage().contains("Bad Request") || e.getMessage().contains("Game not found")) {
                res.status(400);
            } else {
                res.status(500);
            }
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
    private record ListGamesResult(Collection<GameData> games) {}

}
