package server;

import com.google.gson.Gson;
import model.GameData;
import service.GameService;
import service.CreateGameRequest;
import service.CreateGameResult;
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
            if (e.getMessage().contains("Unauthorized")) {
                res.status(401);
            } else if (e.getMessage().contains("Bad Request")) {
                res.status(400);
            } else {
                res.status(500);
            }
            return gson.toJson(new ErrorResponse(e.getMessage()));

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

    private record ErrorResponse(String message) {}
    private record ListGamesResult(Collection<GameData> games) {}

}
