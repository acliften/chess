package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, GameData> games;
    private int gcounter;


    public MemoryGameDAO(){
        games = new HashMap<>();
        gcounter = 0;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        gcounter++;
        GameData data = new GameData(gcounter, null, null, gameName, game);
        games.put(gcounter, data);
        return gcounter;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(int gameID, GameData update) throws DataAccessException {
        games.put(gameID, update);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
        gcounter = 0;
    }
}
