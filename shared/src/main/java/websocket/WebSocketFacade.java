package websocket;

import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.Gson;

public class WebSocketFacade extends Endpoint {

    private Session session;
    private Gson gson = new Gson();

    public WebSocketFacade(String url) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
        } catch (DeploymentException | IOException e){
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String s) {
                ServerMessage serverMessage = gson.fromJson(s, ServerMessage.class);
                switch (serverMessage.getServerMessageType()){
                    case LOAD_GAME -> {
                        //draw chessboard from server
                        System.out.println("game recieved");
                    }
                    case NOTIFICATION -> {
                        System.out.println("serverMessage.");
                    }
                }
            }
        });
    }

    public void connect(String authToken, int gameID){
        try {
            UserGameCommand cmd = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            session.getBasicRemote().sendText(gson.toJson(cmd));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove(){

    }

    public void leave(){

    }

    public void resign(){

    }

}
