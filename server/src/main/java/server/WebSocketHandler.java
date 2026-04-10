package server;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.SQLGameDAO;
import dataaccess.UserDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static websocket.ResponseException.fromJson;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private static final Map<Integer, Set<WsContext>> gameInfo = new HashMap<>();
    private Map<WsContext, String> users = new HashMap<>();
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO){
        WebSocketHandler.gameDAO = gameDAO;
        WebSocketHandler.authDAO = authDAO;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        Gson gson = new Gson();
        UserGameCommand cmd = gson.fromJson(wsMessageContext.message(), UserGameCommand.class);

        switch (cmd.getCommandType()){
            case CONNECT -> {
                gameInfo.putIfAbsent(cmd.getGameID(), new HashSet<>());
                gameInfo.get(cmd.getGameID()).add(wsMessageContext);
                System.out.println("Connecting to game " + cmd.getGameID());
                // Send LOAD_GAME message (required by tests)

                GameData game = gameDAO.getGame(cmd.getGameID());
                users.put(wsMessageContext, authDAO.getAuth(cmd.getAuthToken()).username());

                String loadGameMessage = new Gson().toJson(Map.of(
                        "serverMessageType", "LOAD_GAME",
                        "game", game
                ));
                wsMessageContext.send(loadGameMessage);
            }
            case MAKE_MOVE -> {
                cmd = gson.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                String username = authDAO.getAuth(cmd.getAuthToken()).username();

                GameData game = gameDAO.getGame(cmd.getGameID());


                System.out.println("made move");
                messageUsers(cmd.getGameID(), username + "made move");
            }
            case LEAVE -> {
                System.out.println("user leaving");
                if (gameInfo.get(cmd.getGameID()) != null){
                    gameInfo.get(cmd.getGameID()).remove(wsMessageContext);
                }
                //need to get username
                String username = authDAO.getAuth(cmd.getAuthToken()).username();
                messageUsers(cmd.getGameID(), username + "left the game");
            }
            case RESIGN -> {

                System.out.println("User resigning");
            }
        }

        System.out.println("Received: " + wsMessageContext.message());

    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
        for (Set<WsContext> sessions : gameInfo.values()){
            sessions.removeIf(wsc -> wsc.sessionId().equals(wsCloseContext.sessionId()));
        }
    }

    private void messageUsers(int gameID, String message){
        Set<WsContext> sessions = gameInfo.get(gameID);
        if (sessions == null){
            return;
        }

        String json = new Gson().toJson(Map.of(
                "serverMessageType", "NOTIFICATION",
                "message", message
        ));

        for (WsContext wsc : sessions){
            wsc.send(json);
        }
    }
}
