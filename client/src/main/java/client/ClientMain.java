package client;

import chess.*;
import ui.Client;
import websocket.ResponseException;

public class ClientMain {
    public static void main(String[] args) throws ResponseException {
        Client client = new Client("http://localhost:8080");
        client.run();
    }
}
