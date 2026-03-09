package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import service.ClearService;
import service.GameService;
import service.UserService;

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

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);

    }

}
