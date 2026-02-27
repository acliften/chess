package server;

import com.google.gson.Gson;
import service.GameService;

public class GameHandler {
    GameService gameService = new GameService();
    Gson serializer = new Gson();
}
