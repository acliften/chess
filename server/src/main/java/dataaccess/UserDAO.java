package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public interface UserDAO {

    //create crud methods

    //createUser
    public void createUser(UserData user) throws DataAccessException;

    public UserData getUser(String username) throws DataAccessException;

    public void clear() throws DataAccessException;


}
