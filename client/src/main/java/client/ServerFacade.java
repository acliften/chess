package client;

import records.*;

import java.util.HashMap;

public class ServerFacade {

    ClientCommunicator server;
    String authToken;
    //String serverURL;
    HashMap<Integer, GameList> games;

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

    public String createGame(String[] params) throws ResponseException {
        String gameName = params[0];
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);

        CreateGameResult result = server.makeRequest("POST", "/game", request, CreateGameResult.class, authToken);
        return "Game created";

    }

    public String listGames() throws ResponseException {
//        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = server.makeRequest("GET", "/game", null, ListGamesResult.class, authToken);
        games.clear();

        int i = 1;
        for (GameList game : result.games()){
            String white = (game.whiteUsername() == null) ? "(untaken)" : game.whiteUsername();
            String black = (game.blackUsername() == null) ? "(untaken)" : game.blackUsername();
            System.out.println(i + ". " + game.gameName() + ", white user: " + white + ", black user: " + black);

            games.put(i, game);
            i++;
        }
        return "";

    }

    public String joinGame(String[] params) throws ResponseException {
        int gameNumber = Integer.parseInt(params[0]);
        String color = params[1];
        JoinGameRequest request = new JoinGameRequest(authToken, color, games.get(gameNumber).gameID());

        server.makeRequest("PUT", "/game", request, null, authToken);
        return "Joined game " + gameNumber;
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
