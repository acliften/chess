package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    private final String createStatements =
        """
        CREATE TABLE IF NOT EXISTS user_data (
            username VARCHAR(256) PRIMARY KEY,
            password VARCHAR(256) NOT NULL,
            email VARCHAR(256) NOT NULL
            )
        """;

    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(createStatements)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to create user table");
        }

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user_data (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to create user");
        }

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user_data WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                    return null;
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to get UserData");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user_data WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                    return null;
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to get UserData");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM user_data";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unable to delete data");
        }
    }
}
