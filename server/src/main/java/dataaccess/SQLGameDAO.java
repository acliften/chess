package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO{

    private final Gson serializer = new Gson();

    private final String createStatements =
            """
            CREATE TABLE IF NOT EXISTS game_data (
                gameID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(256) NULL,
                blackUsername VARCHAR(256) NULL,
                gameName VARCHAR(256) NOT NULL,
                game TEXT NOT NULL
                )
            """;

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            try (PreparedStatement preparedStatement = conn.prepareStatement(createStatements)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to create game table");
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game_data (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        ChessGame game = new ChessGame();
        String jsonGame = serializer.toJson(game);
        try (var conn = DatabaseManager.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, gameName);
                ps.setString(4, jsonGame);
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to create game");
        }
        throw new DataAccessException("Error: unable to create game");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game_data WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try(ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new GameData(rs.getInt("gameID"),
                                            rs.getString("whiteUsername"),
                                            rs.getString("blackUsername"),
                                            rs.getString("gameName"),
                                            serializer.fromJson(rs.getString("game"), ChessGame.class));
                    }
                    return null;
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to get GameData");
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game_data";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                try(ResultSet rs = ps.executeQuery()){
                    List<GameData> games = new ArrayList<>();
                    while (rs.next()){
                        GameData data = new GameData(rs.getInt("gameID"),
                                                     rs.getString("whiteUsername"),
                                                     rs.getString("blackUsername"),
                                                     rs.getString("gameName"),
                                                     serializer.fromJson(rs.getString("game"), ChessGame.class));
                        games.add(data);
                    }
                    return games;
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to get GameData");
        }
    }

    @Override
    public void updateGame(int gameID, GameData update) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "UPDATE game_data SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
            String jsonGame = serializer.toJson(update.game());
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, update.whiteUsername());
                ps.setString(2, update.blackUsername());
                ps.setString(3, jsonGame);
                ps.setInt(4, gameID);
                int rows = ps.executeUpdate();
                if (rows == 0){
                    throw new DataAccessException("Error: game does not exist");
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to get update game");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM game_data";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to delete data");
        }
    }
}
