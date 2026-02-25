package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import records.*;
import service.UserService;

public class UserHandler {
    UserService userService = new UserService();
    Gson serializer = new Gson();

    public void register(Context ctx){
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);

        ctx.result(serializer.toJson(result));
    }

    public void login(Context ctx){
        LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);

        ctx.result(serializer.toJson(result));
    }

    public void logout(Context ctx){
        LogoutRequest request = new LogoutRequest(ctx.header("authorization"));
        userService.logout(request);
    }

}
