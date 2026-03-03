package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import records.*;
import service.GameService;

import java.util.HashMap;
import java.util.Map;

public class GameHandler {
    private final GameService gameService;
    Gson serializer = new Gson();

    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public void listGames(Context ctx){
        try{
            ListGamesRequest request = new ListGamesRequest(ctx.header("authorization"));
            ListGamesResult result = gameService.listGames(request);

            ctx.result(serializer.toJson(result));
        } catch (DataAccessException e){
            Map<String, String> error = new HashMap<>();
            ctx.status(401);
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }

    }

    public void createGame(Context ctx) throws DataAccessException {
        try{
            String auth = ctx.header("authorization");
            CreateGameRequest body = serializer.fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameRequest request = new CreateGameRequest(auth, body.gameName());
            CreateGameResult result = gameService.createGame(request);

            ctx.result(serializer.toJson(result));
        } catch(DataAccessException e){
            Map<String, String> error = new HashMap<>();
            if (e.getMessage().contains("bad request")) {
                ctx.status(400);
            } else if (e.getMessage().contains("unauthorized")) {
                ctx.status(401);
            }
            error.put("message", e.getMessage());
            ctx.result(serializer.toJson(error));
        }

    }

    public void joinGame(Context ctx) throws DataAccessException{
        try{
            String auth = ctx.header("authorization");
            JoinGameRequest body = serializer.fromJson(ctx.body(), JoinGameRequest.class);
            JoinGameRequest request = new JoinGameRequest(auth, body.playerColor(), body.gameID());
            gameService.joinGame(request);
        } catch(DataAccessException e){
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
}
