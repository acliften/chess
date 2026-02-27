package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import records.*;
import service.UserService;

public class UserHandler {
    UserService userService = new UserService();
    Gson serializer = new Gson();

    public void register(Context ctx) throws DataAccessException {
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);

        ctx.result(serializer.toJson(result));
    }

    public void login(Context ctx) throws DataAccessException {
        LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);

        ctx.result(serializer.toJson(result));
    }

    public void logout(Context ctx) throws DataAccessException {
        LogoutRequest request = new LogoutRequest(ctx.header("authorization"));
        userService.logout(request);
    }

}
