package server;

import dataaccess.*;
import io.javalin.*;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();

    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final GameHandler gameHandler = new GameHandler(gameService);

    UserService userService = new UserService(userDAO, authDAO);
    private final UserHandler userHandler = new UserHandler(userService);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userHandler::register)
                .post("/session", userHandler::login)
                .delete("/session", userHandler::logout)
                .get("/game", gameHandler::listGames)
                .post("/game", gameHandler::createGame)
                .put("/game", gameHandler::joinGame);

        // Register your endpoints and exception handlers here.


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
