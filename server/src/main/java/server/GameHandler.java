package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import records.*;
import service.GameService;

public class GameHandler {
    private final GameService gameService;
    Gson serializer = new Gson();

    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public void listGames(Context ctx) throws DataAccessException {
        ListGamesRequest request = new ListGamesRequest(ctx.header("authorization"));
        ListGamesResult result = gameService.listGames(request);

        ctx.result(serializer.toJson(result));
    }

    public void createGame(Context ctx) throws DataAccessException {
        //check if this is the right way to get header + body
        String auth = ctx.header("authorization");
        CreateGameRequest body = serializer.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameRequest request = new CreateGameRequest(auth, body.gameName());
        CreateGameResult result = gameService.createGame(request);

        ctx.result(serializer.toJson(result));
    }

    public void joinGame(Context ctx) throws DataAccessException{
        //check if this is the right way to get header + body
        String auth = ctx.header("authorization");
        JoinGameRequest body = serializer.fromJson(ctx.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(auth, body.playerColor(), body.gameID());
        gameService.joinGame(request);
    }
}
