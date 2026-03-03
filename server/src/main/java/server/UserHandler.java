package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import records.*;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private final UserService userService;
    Gson serializer = new Gson();

    public UserHandler(UserService userService){
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);

            ctx.result(serializer.toJson(result));
        } catch(DataAccessException e){
            Map<String, String> error = new HashMap<>();
            if (e.getMessage().contains("bad request")){
                ctx.status(400);
            } else if (e.getMessage().contains("already taken")) {
                ctx.status(403);
            }
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }
    }

    public void login(Context ctx) {
        try {
            LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(request);

            ctx.result(serializer.toJson(result));
        } catch (DataAccessException e){
            Map<String, String> error = new HashMap<>();
            if (e.getMessage().contains("bad request")){
                ctx.status(400);
            } else if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
            }
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }

    }

    public void logout(Context ctx) {
        try {
            LogoutRequest request = new LogoutRequest(ctx.header("authorization"));
            userService.logout(request);
        } catch(DataAccessException e){
            Map<String, String> error = new HashMap<>();
            ctx.status(401);
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }

    }

}
