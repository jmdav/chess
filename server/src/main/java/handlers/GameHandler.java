package handlers;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

public class GameHandler {

  Gson serializer = new Gson();

  private final GameService gameService;

  public GameHandler(GameService gameService) {
    this.gameService = gameService;
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
    AuthData output = userService.login(userData);
    ctx.status(201);
    String jsonOutput = serializer.toJson(output);
    ctx.result(jsonOutput);
  }

  public void logout(Context ctx) throws DataAccessException {
    AuthData userData = serializer.fromJson(ctx.body(), AuthData.class);
    userService.logout(userData);
    ctx.status(201);
  }

  public void destroy(Context ctx) throws DataAccessException {
    userService.destroy();
  }
}
