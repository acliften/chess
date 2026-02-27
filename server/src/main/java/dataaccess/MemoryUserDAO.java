package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    HashMap<String, UserData> users;

    public MemoryUserDAO(){
        users = new HashMap<>();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        return users.get(username);
    }

    @Override
    public void clear() throws DataAccessException{
        users.clear();
    }
}
