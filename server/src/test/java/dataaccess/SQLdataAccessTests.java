package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class SQLdataAccessTests {
    private SQLUserDAO userDAO;
    private SQLAuthDAO authDAO;
    private SQLGameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "user");
        authDAO.createAuth(newAuth);

        AuthData auth2 = authDAO.getAuth("token");
        assertEquals(newAuth, auth2);
    }

    @Test
    public void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("same token", "user");
        authDAO.createAuth(auth);

        AuthData auth2 = new AuthData("same token", "user2");
        assertThrows(DataAccessException.class, ()->authDAO.createAuth(auth2));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "user");
        authDAO.createAuth(newAuth);

        AuthData auth2 = authDAO.getAuth("token");
        assertEquals(newAuth, auth2);

    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "user");
        authDAO.createAuth(newAuth);

        assertNull(authDAO.getAuth("wrong token"));
    }

    @Test
    public void clearAuthPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("token", "user");
        authDAO.createAuth(newAuth);
        authDAO.clear();

        assertNull(authDAO.getAuth("token"));
    }




    @Test
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "password", "email");
        userDAO.createUser(user);
        UserData getuser = userDAO.getUser("user");

        assertEquals(user, getuser);
    }

    @Test
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("user", "password", "email");
        userDAO.createUser(user);

        UserData user2 = new UserData("user", "password", "email");
        assertThrows(DataAccessException.class, ()-> userDAO.createUser(user2));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "password", "email");
        userDAO.createUser(user);

        UserData getuser = userDAO.getUser("user");
        assertEquals(user, getuser);
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        UserData user = new UserData("user", "password", "email");
        userDAO.createUser(user);

        assertNull(userDAO.getUser("wrong user"));
    }

    @Test
    public void clearUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "password", "email");
        userDAO.createUser(user);

        userDAO.clear();

        assertNull(userDAO.getUser("user"));
    }




    @Test
    public void createGamePositive() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData game1 = new GameData(1, null, null, "game1", game);
        int gameID = gameDAO.createGame(game1.gameName());

        GameData getGame = gameDAO.getGame(gameID);
        assertEquals("game1", getGame.gameName());
    }

    @Test
    public void createGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, ()-> gameDAO.createGame(null));
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");

        assertInstanceOf(GameData.class, gameDAO.getGame(gameID));
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");

        assertNull(gameDAO.getGame(gameID + 67));
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");

        var games = gameDAO.listGames();

        assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        var games = gameDAO.listGames();

        assertEquals(0, games.size());
    }

    @Test
    public void updateGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");

        GameData update = new GameData(gameID, "big yahu", null, "game1", new ChessGame());
        gameDAO.updateGame(gameID, update);

        GameData updatedGame = gameDAO.getGame(gameID);

        assertEquals("big yahu", updatedGame.whiteUsername());
    }

    @Test
    public void updateGameNegative() throws DataAccessException {
        GameData update = new GameData(69, "big yahu", null, "game1", new ChessGame());
        assertThrows(DataAccessException.class, ()-> gameDAO.updateGame(update.gameID(), update));

    }

    @Test
    public void clearGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("game1");
        gameDAO.clear();

        assertNull(gameDAO.getGame(gameID));
    }

}
