package service;
import records.*;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
        return null;
    }
    public LoginResult login(LoginRequest loginRequest) {
        return new LoginResult("userrrr", "realtoken");
    }
    public void logout(LogoutRequest logoutRequest) {
        
    }
}

