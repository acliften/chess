package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import dataaccess.*;
import model.AuthData;
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
        if (request == null || request.gameName() == null){
            throw new DataAccessException("Error: bad request");
        } else if (request.authorization() == null){
            throw new DataAccessException("Error: unauthorized");
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

    public GameData makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) throw new DataAccessException("Error: unauthorized");
        String username = auth.username();

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) throw new DataAccessException("Error: game does not exist");

        if (gameData.game().isOver()) {
            throw new DataAccessException("Error: game is over");
        }

        ChessGame chessGame = gameData.game();
        ChessPiece pieceAtStart = chessGame.getBoard().getPiece(move.getStartPosition());

        if (pieceAtStart == null) throw new DataAccessException("Error: no piece at start position");

        String colorAtStart = pieceAtStart.getTeamColor().toString(); // "WHITE" or "BLACK"
        String playerColor = (username.equals(gameData.whiteUsername())) ? "WHITE" :
                (username.equals(gameData.blackUsername())) ? "BLACK" : "OBSERVER";

        if (!colorAtStart.equals(playerColor)) {
            throw new DataAccessException("Error: cannot move opponent's piece");
        }

        if (!chessGame.getTeamTurn().toString().equals(playerColor)) {
            throw new DataAccessException("Error: not your turn");
        }
        try {
            chessGame.makeMove(move);
        } catch (Exception e) {
            throw new DataAccessException("Error: invalid move");
        }
        gameDAO.updateGame(gameID, gameData);

        return gameData;
    }

    public void resign(String authToken, int gameID) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) throw new DataAccessException("Error: unauthorized");

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) throw new DataAccessException("Error: bad gameID");

        String username = auth.username();
        if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
            throw new DataAccessException("Error: observer cannot resign");
        }


        if (gameData.game().isOver()) {
            throw new DataAccessException("Error: game is already over");
        }

        gameData.game().setOver(true);

        gameDAO.updateGame(gameID, gameData);
    }

}
