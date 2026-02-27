package dataaccess;

import chess.ChessGame;
import java.util.List;

public interface GameDAO {

    void createGame() throws DataAccessException;

    ChessGame getGame() throws DataAccessException;

    List<ChessGame> listGames() throws DataAccessException;

    void updateGame() throws DataAccessException;

}
