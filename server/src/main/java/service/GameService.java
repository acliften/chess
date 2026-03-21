package service;

import dataaccess.*;
import model.GameData;
import records.*;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {

        if (authDAO.getAuth(request.authorization()) != null){
            List<GameList> list = new ArrayList<>();
            for (GameData game : gameDAO.listGames()) {
                list.add(new GameList(
                        game.gameID(),
                        game.whiteUsername(),
                        game.blackUsername(),
                        game.gameName()
                ));
            }
            return new ListGamesResult(list);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException{
        if (request == null || request.gameName() == null || request.authorization() == null){
            throw new DataAccessException("Error: bad request");
        }
        if (authDAO.getAuth(request.authorization()) != null){
            return new CreateGameResult(gameDAO.createGame(request.gameName()));
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException{
        if ( request == null || request.playerColor() == null){
            throw new DataAccessException("Error: bad request");
        }

        if (authDAO.getAuth(request.authorization()) != null){
            GameData gameData = gameDAO.getGame(request.gameID());
            if (gameData == null){
                throw new DataAccessException("Error: bad request");
            }
            String user = authDAO.getAuth(request.authorization()).username();

            if (request.playerColor().equals("BLACK")){
                if (gameData.blackUsername() == null){
                    gameDAO.updateGame(request.gameID(),
                            new GameData(gameData.gameID(), gameData.whiteUsername(), user, gameData.gameName(), gameData.game()));
                } else if (!gameData.blackUsername().equals(user)){
                    throw new DataAccessException("Error: already taken");
                }
            } else if (request.playerColor().equals("WHITE")){
                if (gameData.whiteUsername() == null){
                    gameDAO.updateGame(request.gameID(),
                            new GameData(gameData.gameID(), user, gameData.blackUsername(), gameData.gameName(), gameData.game()));
                } else if (!gameData.whiteUsername().equals(user)){
                    throw new DataAccessException("Error: already taken");
                }
            } else if (request.playerColor().equals("OBSERVER")){
                return;
            } else {
                throw new DataAccessException("Error: bad request");
            }
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

}
