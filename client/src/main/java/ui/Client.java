package ui;

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
            } catch (Throwable e){
                System.out.println(e.toString());
            }

        }
    }

    public String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> sf.register(params);
                case "login" -> sf.login(params);
                case "create" -> sf.createGame(params);
                case "list" -> sf.listGames();
                case "join" -> sf.joinGame(params);
                case "observe" -> sf.observeGame(params);
                case "logout" -> sf.logout();
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e){
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
