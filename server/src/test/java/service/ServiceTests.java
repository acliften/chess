package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import records.CreateGameRequest;
import records.JoinGameRequest;
import records.RegisterRequest;
import records.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private MemoryGameDAO memoryGameDAO;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

//    ServiceTests(MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO){
//
//    }

    @BeforeEach
    public void setup() {
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();
        memoryGameDAO = new MemoryGameDAO();

        userService = new UserService(memoryUserDAO, memoryAuthDAO);
        gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        clearService = new ClearService(memoryUserDAO, memoryAuthDAO, memoryGameDAO);

    }

    @Test
    public void clearPostitive() throws DataAccessException {
        userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        gameService.createGame(new CreateGameRequest("auth", "game1"));
        clearService.clear();
        assert(memoryUserDAO.getUser("bibi") == null);
        assert (memoryGameDAO.listGames() == null);
    }

    @Test
    public void createUserPostitive(){

    }

    @Test
    public void createUserNegative(){

    }

    //check if you can register user
    @Test
    public void registerPostitive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);

        assertNotNull(result.authToken());
        assertEquals("bibi", result.username());
    }

    //check if same userInfo can register twice
    @Test
    public void registerNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        userService.register(request);

        assertThrows(DataAccessException.class, () -> userService.register(request));
    }

    @Test
    public void joinGamePositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);

        int gameID = gameService.createGame(new CreateGameRequest(result.authToken(), "game1")).gameID();

        gameService.joinGame(new JoinGameRequest(result.authToken(), "BLACK", gameID));

        GameData game = memoryGameDAO.getGame(gameID);

        assertEquals("bibi", game.blackUsername());

    }

    //test to make sure you can't steal team color
    @Test
    public void joinGameNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);
        int gameID = gameService.createGame(new CreateGameRequest(result.authToken(), "game1")).gameID();
        gameService.joinGame(new JoinGameRequest(result.authToken(), "BLACK", gameID));

        RegisterRequest request2 = new RegisterRequest("donnyT", "ilovebubba", "trump@trump.com");
        RegisterResult result2 = userService.register(request2);
        assertThrows(DataAccessException.class, () -> gameService.joinGame(new JoinGameRequest(result2.authToken(), "BLACK", gameID)));
    }




}

