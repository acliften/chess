package server;

import dataaccess.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();

    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final GameHandler gameHandler = new GameHandler(gameService);

    private final UserService userService = new UserService(userDAO, authDAO);
    private final UserHandler userHandler = new UserHandler(userService);

    private final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    private final ClearHandler clearHandler = new ClearHandler(clearService);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userHandler::register)
                .post("/session", userHandler::login)
                .delete("/session", userHandler::logout)
                .get("/game", gameHandler::listGames)
                .post("/game", gameHandler::createGame)
                .put("/game", gameHandler::joinGame)
                .delete("/db", clearHandler::clear);

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
