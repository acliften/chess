package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {

    private final Gson serializer = new Gson();

    public void handleErrors(DataAccessException e, Context ctx){
        Map<String, String> error = new HashMap<>();
        if (e.getMessage().contains("bad request")){
            ctx.status(400);
        } else if (e.getMessage().contains("unauthorized")) {
            ctx.status(401);
        } else if (e.getMessage().contains("already taken")) {
            ctx.status(403);
        }
        error.put("message", e.getMessage());
        ctx.result(serializer.toJson(error));
    }


}
