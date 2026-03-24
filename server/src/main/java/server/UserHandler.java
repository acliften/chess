package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import records.*;
import service.UserService;

public class UserHandler {
    private final UserService userService;
    private final Gson serializer = new Gson();
    private ErrorHandler errorHandler = new ErrorHandler();

    public UserHandler(UserService userService){
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);

            ctx.result(serializer.toJson(result));
        } catch(DataAccessException e){
            errorHandler.handleErrors(e, ctx);
        }
    }

    public void login(Context ctx) {
        try {
            LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(request);

            ctx.result(serializer.toJson(result));
        } catch (DataAccessException e){
            errorHandler.handleErrors(e, ctx);
        }

    }

    public void logout(Context ctx) {
        try {
            LogoutRequest request = new LogoutRequest(ctx.header("authorization"));
            userService.logout(request);
        } catch(DataAccessException e){
            errorHandler.handleErrors(e, ctx);
        }

    }

}
