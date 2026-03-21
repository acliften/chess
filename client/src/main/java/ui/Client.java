package ui;

import client.ResponseException;
import client.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class Client {

    boolean loggedIn = false;
    ServerFacade sf = new ServerFacade();

    public void run() {
        System.out.println("hi");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            System.out.print("\n" + ">>> ");
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(result);
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
                case "join" -> sf.joinGame(params);
                case "observe" -> sf.observeGame(params);
                case "logout" -> {
                    String result = sf.logout();
                    loggedIn = false;
                    yield result;
                }
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    public String help() {
        if (!loggedIn) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD>
                    - quit - exit program
                    - help - receive this instruction again
                    """;
        }
        return """
                - create <GAME_NAME> - creates a game
                - list - lists available games
                - join <ID> [WHITE|BLACK] - joins a game
                - observe <ID> - spectate a game
                - logout - log out of your account
                - quit - exit program
                - help - shows this list again
                """;
    }

}
