package client;

import org.junit.jupiter.api.*;
import server.Server;
import org.junit.jupiter.api.Test;
import websocket.ResponseException;
import websocket.WebSocketFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sf;
    private static String url;
    private static WebSocketFacade ws;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        url = "http://localhost:" + port;
        sf = new ServerFacade(url, ws);
    }

    @BeforeEach
    void setUp() throws ResponseException {
        sf.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive(){
        assertDoesNotThrow(()-> sf.register(new String[] {"user1", "password", "e@mail.com"}));
    }

    @Test
    public void registerNegative(){
        assertThrows(ResponseException.class, ()-> {sf.register(new String[] {"user1", "password", "e@mail.com"});
                                                    sf.register(new String[] {"user1", "password", "e@mail.com"});});
    }

    @Test
    public void loginPositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        assertDoesNotThrow(()-> sf.login(new String[]{"user1", "password"}));
    }

    @Test
    public void loginNegative() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        assertThrows(ResponseException.class, ()-> sf.login(new String[]{"user1", "wrongpassword"}));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        assertDoesNotThrow(()-> sf.logout());
    }

    @Test
    public void logoutNegative(){
        assertThrows(ResponseException.class, ()-> sf.logout());
    }

    @Test
    public void createPositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        assertDoesNotThrow(()-> sf.createGame(new String[]{"game1"}));
    }

    @Test
    public void createNegative(){
        assertThrows(ResponseException.class, ()-> sf.createGame(new String[]{"game1"}));
    }

    @Test
    public void listPositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.createGame(new String[]{"game1"});
        assertNull(sf.listGames());
    }

    @Test
    public void listNegative() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.logout();
        assertThrows(ResponseException.class, ()-> sf.listGames());
    }

    @Test
    public void joinPositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.createGame(new String[]{"game1"});
        sf.listGames();
        assertDoesNotThrow(()-> sf.joinGame(new String[]{"1", "WHITE"}));
    }

    @Test
    public void joinNegative() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.createGame(new String[]{"game1"});
        sf.listGames();
        assertThrows(ResponseException.class, ()-> sf.joinGame(new String[]{"67", "WHITE"}));
    }

    @Test
    public void observePositive() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.createGame(new String[]{"game1"});
        sf.listGames();
        assertDoesNotThrow(()-> sf.observeGame(new String[]{"1"}));
    }

    @Test
    public void observeNegative() throws ResponseException {
        sf.register(new String[] {"user1", "password", "e@mail.com"});
        sf.createGame(new String[]{"game1"});
        sf.listGames();
        assertThrows(ResponseException.class, ()-> sf.observeGame(new String[]{"31"}));
    }



}
