package server;

import com.google.gson.Gson;
import service.*;
import spark.Request;
import spark.Response;
import dataaccess.DataAccessException;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) {
        try {
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

            if (registerRequest.password() == null || registerRequest.username() == null || registerRequest.email() == null) {
                res.status(400);
                return gson.toJson(new ErrorResponse("Error: Bad request"));
            }
            var authData = userService.register(registerRequest);

            res.status(200);
            return gson.toJson(new RegisterResult(authData.username(), authData.authToken()));
        } catch (DataAccessException e) {
            if (e.getMessage().contains("already taken")) {
                res.status(403);
            } else if (e.getMessage().contains("Bad Request")) {
                res.status(400);
            } else {
                res.status(500);
            }
            return gson.toJson(new ErrorResponse(e.getMessage()));

        }
    }
    public record ErrorResponse(String message) {}

    public Object login(Request req,Response res) {
        try {
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
            var authData = userService.login(loginRequest.username(), loginRequest.password());
            res.status(200);
            return gson.toJson(new LoginResult(authData.username(), authData.authToken()));
        }
        catch (DataAccessException e) {
            return ErrorHandler.handleDataAccessException(e, res);
        }
    }
    public Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            userService.logout(authToken);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
