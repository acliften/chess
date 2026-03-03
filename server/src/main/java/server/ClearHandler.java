package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class ClearHandler {

    private final ClearService clearService;
    Gson serializer = new Gson();

    public ClearHandler(ClearService clearService){
        this.clearService = clearService;
    }

    public void clear(Context ctx) {
        try{
            clearService.clear();
            ctx.status(200);
            ctx.json("{}");
        } catch (DataAccessException e) {
            Map<String, String> error = new HashMap<>();
            ctx.status(401);
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }

    }

}
