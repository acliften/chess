package server;

import dataaccess.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

public class Server {

    private final Javalin javalin;

    public Server() {
        try {
            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();
            GameService gameService = new GameService(gameDAO, authDAO);
            GameHandler gameHandler = new GameHandler(gameService);

            UserDAO userDAO = new SQLUserDAO();
            UserService userService = new UserService(userDAO, authDAO);
            UserHandler userHandler = new UserHandler(userService);

            ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
            ClearHandler clearHandler = new ClearHandler(clearService);

            // Register your endpoints and exception handlers here.
            javalin = Javalin.create(config -> config.staticFiles.add("web"))
                    .post("/user", userHandler::register)
                    .post("/session", userHandler::login)
                    .delete("/session", userHandler::logout)
                    .get("/game", gameHandler::listGames)
                    .post("/game", gameHandler::createGame)
                    .put("/game", gameHandler::joinGame)
                    .delete("/db", clearHandler::clear);
        } catch (DataAccessException e){
            throw new RuntimeException("Failed to start server", e);
        }

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
