package server;

import io.javalin.*;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserHandler userHandler = new UserHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userHandler::register)
                .post("/session", userHandler::login)
                .delete("/session", userHandler::logout);

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
