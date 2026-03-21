package client;

import records.RegisterRequest;
import records.RegisterResult;

public class ServerFacade {

    ClientCommunicator server;

    public String register(String[] params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email = params[3];
        RegisterRequest request = new RegisterRequest(username, password, email);

        RegisterResult result = server.makeRequest("POST", "/session", request, RegisterResult.class);

        return "";
    }

    public String login(String[] params){
        return "";

    }

    public String createGame(String[] params){
        return "";

    }

    public String listGames(){
        return "";

    }

    public String joinGame(String[] params){
        return "";
    }

    public String observeGame(String[] params){
        return "";
    }

    public String logout(){
        return "";
    }


}
