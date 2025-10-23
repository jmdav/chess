package dataAccess;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import model.AuthData;

public class AuthRAMDAO implements AuthDataAccess {

  private Map<String, AuthData> authDB = new ConcurrentHashMap<>();

  @Override
  public AuthData getSession(String token) throws DataAccessException {
    return authDB.get(token);
  };

  @Override
  public AuthData createSession(String username) throws DataAccessException {
    AuthData data = new AuthData(username, UUID.randomUUID().toString());
    authDB.put(data.authToken(), data);
    return data;
  };

  public AuthData deleteSession(String token) throws DataAccessException {
    return authDB.remove(token);
  };

  @Override
  public void destroy() throws DataAccessException {
    authDB.clear();
  }
}