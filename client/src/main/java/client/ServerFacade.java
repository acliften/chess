package client;

import records.*;

public class ServerFacade {

    ClientCommunicator server;
    String authToken;
    //String serverURL;

    public ServerFacade(String url){
        server = new ClientCommunicator(url);
    }

    public String register(String[] params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest request = new RegisterRequest(username, password, email);

        RegisterResult result = server.makeRequest("POST", "/user", request, RegisterResult.class, null);
        authToken = result.authToken();
        return "Logged in as " + result.username();
    }

    public String login(String[] params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        LoginRequest request = new LoginRequest(username, password);

        LoginResult result = server.makeRequest("POST", "/session", request, LoginResult.class, null);
        authToken = result.authToken();
        return "Logged in as " + result.username();

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

    public String logout() throws ResponseException {
        LogoutRequest request = new LogoutRequest(authToken);
        LoginResult result = server.makeRequest("DELETE", "/session", request, LoginResult.class, authToken);
        return "logged out";
    }


}
