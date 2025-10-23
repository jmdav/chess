package handlers;

import dataAccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

public class UserHandler {

  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  public void register(Context ctx) throws DataAccessException {
    UserData userData = ctx.bodyAsClass(UserData.class);
    AuthData output = userService.register(userData);
    ctx.status(201).json(output);
  }
}
