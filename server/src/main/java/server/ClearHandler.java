package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.security.PublicKey;

public class ClearHandler {

    private final ClearService clearService;
    Gson serializer = new Gson();

    public ClearHandler(ClearService clearService){
        this.clearService = clearService;
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.status(200);
        ctx.json(new Object());
    }

}
