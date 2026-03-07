package dataaccess;

import model.AuthData;

import java.sql.*;

public class SQLAuthDAO implements AuthDAO{

    private final String createStatements =
            """
            CREATE TABLE IF NOT EXISTS auth_data (
            authToken VARCHAR(256) PRIMARY KEY,
            username VARCHAR(256) NOT NULL
            )
            """;

    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(createStatements)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to create auth_table");
        }

    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException{
        var statement = "INSERT INTO auth_data (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to create Authorization");
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM auth_data WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                    return null;
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to get AuthData");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth_data WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to delete data");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth_data";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Unable to delete data");
        }
    }
}
