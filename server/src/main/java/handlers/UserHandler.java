package handlers;

import io.javalin.http.Context;
import model.UserData;
import service.UserService;

public class UserHandler {

  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  public void register(Context ctx) {
    UserData userData = ctx.bodyAsClass(UserData.class);
    UserData output = userService.register(userData);
    ctx.status(201).json(output);
  }
}
