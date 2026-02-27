package service;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import records.*;
import java.util.UUID;

public class UserService {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public UserService(){

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.getUser(registerRequest.username()) == null){
            UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.createUser(user);
            String authToken = generateToken();
            AuthData auth = new AuthData(authToken, user.username());
            authDAO.createAuth(auth);

            return new RegisterResult(user.username(), authToken);
        }
        throw new DataAccessException("Error: already taken");
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData user = userDAO.getUser(loginRequest.username());
        if (user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if (loginRequest.password().equals(user.password())){
            String authToken = generateToken();
            AuthData auth = new AuthData(authToken, user.username());
            authDAO.createAuth(auth);

            return new LoginResult(user.username(), auth.authToken());
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (authDAO.getAuth(logoutRequest.authorization()) != null){
            authDAO.deleteAuth(logoutRequest.authorization());
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}

