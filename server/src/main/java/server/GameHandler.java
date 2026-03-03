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
    private final Gson serializer = new Gson();
    private ErrorHandler errorHandler = new ErrorHandler();

    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public void listGames(Context ctx){
        try{
            ListGamesRequest request = new ListGamesRequest(ctx.header("authorization"));
            ListGamesResult result = gameService.listGames(request);

            ctx.result(serializer.toJson(result));
        } catch (DataAccessException e){
            errorHandler.handleErrors(e, ctx);
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
            errorHandler.handleErrors(e, ctx);
        }

    }

    public void joinGame(Context ctx) throws DataAccessException{
        try{
            String auth = ctx.header("authorization");
            JoinGameRequest body = serializer.fromJson(ctx.body(), JoinGameRequest.class);
            JoinGameRequest request = new JoinGameRequest(auth, body.playerColor(), body.gameID());
            gameService.joinGame(request);
        } catch(DataAccessException e){
            errorHandler.handleErrors(e, ctx);
        }
    }

}
