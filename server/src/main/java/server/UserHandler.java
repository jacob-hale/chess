package server;

import com.google.gson.Gson;
import service.UserService;
import service.RegisterRequest;
import service.RegisterResult;
import spark.Request;
import spark.Response;
import dataaccess.DataAccessException;

import java.util.Objects;

public class UserHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) {
        try {
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

            var authData = userService.register(new model.UserData(
                    registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()
            ));

            res.status(200);
            return gson.toJson(new RegisterResult(authData.username(), authData.authToken()));
        } catch (DataAccessException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        }

    }
    private record ErrorResponse(String message) {}
}
