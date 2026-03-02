package service;

import dataaccess.*;
import records.*;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        if (authDAO.getAuth(request.authorization()) != null){
            ListGamesResult result = new ListGamesResult(gameDAO.listGames());
            return result;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException{

        return null;
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException{

    }
}
