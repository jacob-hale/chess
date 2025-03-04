package server;

import com.google.gson.Gson;
import service.GameService;
import service.CreateGameRequest;
import service.CreateGameResult;
import spark.Request;
import spark.Response;
import dataaccess.DataAccessException;

public class GameHandler {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }
    public Object createGame(Request req, Response res) {
        try {
            CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

            int gameID = gameService.createGame(createGameRequest.gameName());

            res.status(200);
            return gson.toJson(new CreateGameResult(gameID));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("Bad request")) {
                res.status(400);
            } else {
                res.status(500);
            }
            return gson.toJson(new ErrorResponse(e.getMessage()));

        }
    }
    private record ErrorResponse(String message) {}

}
