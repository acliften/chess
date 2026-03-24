package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import records.*;

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
        RegisterResult result = userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        gameService.createGame(new CreateGameRequest(result.authToken(), "game1"));
        clearService.clear();
        assertNull(memoryUserDAO.getUser("bibi"));
        assertTrue(memoryGameDAO.listGames().isEmpty());
        assertNull(memoryAuthDAO.getAuth(result.authToken()));
    }

    //check if user can register
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

    //test new user is able to login
    @Test
    public void loginPostitive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);
        assertDoesNotThrow(() -> userService.login(new LoginRequest("bibi", "amyisraelchai")));
    }

    // test login fails with wrong password
    @Test
    public void loginNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        userService.register(request);
        assertThrows(DataAccessException.class, () -> userService.login(new LoginRequest("bibi", "freepalistine")));

    }

    //test logout works
    @Test
    public void logoutPositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);

        assertDoesNotThrow(() -> userService.logout(new LogoutRequest(result.authToken())));
    }

    //test logout fails with bad request
    @Test
    public void logoutNegative() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com");
        RegisterResult result = userService.register(request);

        assertThrows(DataAccessException.class, () -> userService.logout(new LogoutRequest("fakeAuth")));
    }

    //test that it returns a list of games
    @Test
    public void listGamesPositive() throws DataAccessException{
        RegisterResult user = userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        gameService.createGame(new CreateGameRequest(user.authToken(), "game1"));

        ListGamesRequest request = new ListGamesRequest(user.authToken());
        ListGamesResult result = gameService.listGames(request);

        assertFalse(result.games().isEmpty());
    }

    //test listGame throws with bad auth
    @Test
    public void listGamesNegative() throws DataAccessException{
        RegisterResult user = userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        gameService.createGame(new CreateGameRequest(user.authToken(), "game1"));

        ListGamesRequest request = new ListGamesRequest("fakeAUth");
        assertThrows(DataAccessException.class, () -> gameService.listGames(request));
    }

    //test created game is stored
    @Test
    public void createGamePositive() throws DataAccessException{
        RegisterResult user = userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        CreateGameResult result = gameService.createGame(new CreateGameRequest(user.authToken(), "game1"));
        assertNotNull(memoryGameDAO.getGame(result.gameID()));
    }

    // test it throws when given a bad request
    @Test
    public void createGameNegative() throws DataAccessException{
        RegisterResult user = userService.register(new RegisterRequest("bibi", "amyisraelchai", "bigyahu@shalom.com"));
        assertThrows(DataAccessException.class, () -> gameService.createGame(new CreateGameRequest(user.authToken(), null)));
    }

    //test user can join game
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

