package dataAccess;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import model.AuthData;

public class AuthRAMDAO implements AuthDataAccess {

  private Map<String, AuthData> userDB = new ConcurrentHashMap<>();

  @Override
  public AuthData getSession(String token) throws DataAccessException {
    return userDB.get(token);
  };

  @Override
  public AuthData createSession(String username) throws DataAccessException {
    AuthData data = new AuthData(username, UUID.randomUUID().toString());
    userDB.put(username, data);
    return data;
  };
}