package server;

import com.google.gson.Gson;
import dataaccess.*;
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

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private static final Map<Integer, Set<WsContext>> GAME_INFO = new HashMap<>();
    private Map<WsContext, String> users = new HashMap<>();
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static GameService gs;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO, GameService gs){
        WebSocketHandler.gameDAO = gameDAO;
        WebSocketHandler.authDAO = authDAO;
        WebSocketHandler.gs = gs;
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
                GAME_INFO.putIfAbsent(cmd.getGameID(), new HashSet<>());
                Set<WsContext> sessions = GAME_INFO.get(cmd.getGameID());
                sessions.add(wsMessageContext);

                System.out.println("Connecting to game " + cmd.getGameID());

                GameData game = gameDAO.getGame(cmd.getGameID());
                String username = authDAO.getAuth(cmd.getAuthToken()).username();
                users.put(wsMessageContext, username);

                String loadGameMessage = new Gson().toJson(Map.of(
                        "serverMessageType", "LOAD_GAME",
                        "game", game
                ));
                wsMessageContext.send(loadGameMessage);

                if (sessions.size() > 1){
                    String n = new Gson().toJson(Map.of(
                            "serverMessageType", "NOTIFICATION",
                            "message", username + " joined the game"
                    ));
                    for (WsContext wsc : sessions) {
                        wsc.send(n);
                    }
                }

                messageUsers(cmd.getGameID(), username + " joined as player/observer");
            }
            case MAKE_MOVE -> {
                try {
                    MakeMoveCommand mcmd = gson.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                    String username = authDAO.getAuth(mcmd.getAuthToken()).username();

                    GameData updatedGame = gs.makeMove(mcmd.getAuthToken(), mcmd.getGameID(), mcmd.getMove());

                    sendGame(updatedGame.gameID());
                    messageUsers(mcmd.getGameID(),
                            username + "moved from " + mcmd.getMove().getStartPosition().toString());

                } catch (DataAccessException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
            case LEAVE -> {
                System.out.println("user leaving");
                if (GAME_INFO.get(cmd.getGameID()) != null){
                    GAME_INFO.get(cmd.getGameID()).remove(wsMessageContext);
                }
                //need to get username
                String username = authDAO.getAuth(cmd.getAuthToken()).username();
                messageUsers(cmd.getGameID(), username + "left the game");
            }
            case RESIGN -> {
                String username = authDAO.getAuth(cmd.getAuthToken()).username();

                messageUsers(cmd.getGameID(), username + " resigned");

                System.out.println(username + " resigned");
            }
        }

        System.out.println("Received: " + wsMessageContext.message());

    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
        for (Set<WsContext> sessions : GAME_INFO.values()){
            sessions.removeIf(wsc -> wsc.sessionId().equals(wsCloseContext.sessionId()));
        }
    }

    private void messageUsers(int gameID, String message){
        Set<WsContext> sessions = GAME_INFO.get(gameID);
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

    private void sendGame(int gameID) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID);

        String json = new Gson().toJson(Map.of(
                "serverMessageType", "LOAD_GAME",
                "game", game
        ));

        Set<WsContext> sessions = GAME_INFO.get(gameID);
        if (sessions == null){
            return;
        }

        for (WsContext ws : sessions) {
            ws.send(json);
        }
    }

}
