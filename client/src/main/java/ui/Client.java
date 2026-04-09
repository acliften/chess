package ui;

import chess.ChessGame;
import websocket.ResponseException;
import client.ServerFacade;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    boolean loggedIn = false;
    boolean inGame = false;
    ServerFacade sf;
    ChessBoard cb = new ChessBoard();
    WebSocketFacade ws;

    public Client(String url) throws ResponseException {
        this.ws = new WebSocketFacade(url);
        this.sf = new ServerFacade(url, ws);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!"quit".equals(result)){
            System.out.print( ">>> ");
            String line = scanner.nextLine();

            try {
                result = eval(line);
                if (result != null) {
                    System.out.println(result);
                }
            } catch (Exception e){
                System.out.println(e.toString());
            }

        }
    }

    public String eval(String input){
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> {
                    String result = sf.register(params);
                    loggedIn = true;
                    yield result;
                }
                case "login" -> {
                    String result = sf.login(params);
                    loggedIn = true;
                    yield result;
                }
                case "create" -> sf.createGame(params);
                case "list" -> sf.listGames();
                case "join" -> {
                    if (params.length < 2){
                        yield "Error: include <ID> [WHITE|BLACK]";
                    }
                    ChessGame game = sf.joinGame(params);
                    cb.drawChessboard(game, Objects.equals(params[1], "BLACK"));
                    inGame = true;
                    loggedIn = false;
                    yield null;
                }
                case "observe" -> {
                    ChessGame game = sf.observeGame(params);
                    cb.drawChessboard(game, false);
                    yield null;
                }
                case "logout" -> {
                    String result = sf.logout();
                    loggedIn = false;
                    yield result;
                }
                case "help" -> help();
                case "quit" -> "quit";

                case "redraw" -> "";
                case "leave" -> {
                    loggedIn = true;
                    inGame = false;
                    yield null;
                }
                case "move" -> " ";
                case "resign" -> null;
                case "highlight" -> null;
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String help() {
        if (loggedIn) {
            return """
                    - create <GAME_NAME> | creates a game
                    - list | lists available games
                    - join <ID> [WHITE|BLACK] | joins a game
                    - observe <ID> | spectate a game
                    - logout | log out of your account
                    - quit | exit program
                    - help | list of available actions""";

        } else if (inGame) {
            return """
                    - help | list of available actions
                    - redraw | redraws the chessboard
                    - leave | exits game
                    - move <FROM> <TO> | make move. example "move a7 a6"
                    - resign | player forfiets the game ends
                    - highlight | highllights legal moves""";
        }
        return """
                    - register <USERNAME> <PASSWORD> <EMAIL> | to create an account
                    - login <USERNAME> <PASSWORD>
                    - quit | exit program
                    - help | list of available actions""";
    }

}
