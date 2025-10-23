package handlers;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

public class UserHandler {

  Gson serializer = new Gson();

  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  public void register(Context ctx) throws DataAccessException {
    UserData userData = serializer.fromJson(ctx.body(), UserData.class);
    AuthData output = userService.register(userData);
    ctx.status(201);
    String jsonOutput = serializer.toJson(output);
    System.out.println(jsonOutput);
    ctx.result(jsonOutput);
  }
}
