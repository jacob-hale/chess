package server;

import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var userService = new UserService(userDAO, authDAO);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.post("/user", (req, res) -> new UserHandler(userService).register(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
