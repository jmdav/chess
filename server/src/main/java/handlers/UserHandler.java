package handlers;
import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import io.javalin.http.Context;
import javax.sound.midi.SysexMessage;
import model.AuthData;
import model.UserData;
import service.UserService;

public class UserHandler {

  Gson serializer = new Gson();

  private final UserService userService;

  public UserHandler(UserService userService, AuthDataAccess authDataAccess) {
    this.userService = userService;
  }

  public void register(Context ctx) throws DataAccessException {
    UserData userData = serializer.fromJson(ctx.body(), UserData.class);
    AuthData output = userService.register(userData);
    ctx.status(201);
    String jsonOutput = serializer.toJson(output);
    ctx.result(jsonOutput);
  }

  public void login(Context ctx) throws DataAccessException {
    UserData userData = serializer.fromJson(ctx.body(), UserData.class);
    System.out.println(userData);
    AuthData output = userService.login(userData);
    ctx.status(201);
    String jsonOutput = serializer.toJson(output);
    ctx.result(jsonOutput);
  }

  public void logout(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    userService.logout(authToken);
    ctx.status(201);
  }

  public void destroy(Context ctx) throws DataAccessException {
    userService.destroy();
  }
}
