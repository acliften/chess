package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{

    HashMap<String, AuthData> authorizations;

    public MemoryAuthDAO(){
        authorizations = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        authorizations.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authorizations.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authorizations.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authorizations.clear();
    }
}
