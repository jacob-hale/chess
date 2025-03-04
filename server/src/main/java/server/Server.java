package server;

import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import service.ClearService;
import service.UserService;
import service.GameService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var gameDao = new MemoryGameDAO();
        var userService = new UserService(userDAO, authDAO);
        var clearService = new ClearService(userDAO, authDAO);
        var gameService = new GameService(gameDao);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.delete("/db", (req, res) -> {
            clearService.clear();
            res.status(200);
            return "{}";
        });
        Spark.post("/user", (req, res) -> new UserHandler(userService).register(req, res));

        Spark.post("/session", (req, res) -> new UserHandler(userService).login(req, res));

        Spark.delete("/session", (req, res) -> new UserHandler(userService).logout(req, res));

        Spark.post("/game", (req, res) -> new GameHandler(gameService).createGame(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
