package handlers;

import com.google.gson.Gson;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

public class UserHandler {

  Gson serializer = new Gson();

  private final UserService userService;

  public UserHandler(UserService userService, AuthDataAccess authDataAccess) {
    this.userService = userService;
  }

  public void register(Context ctx) {
    UserData userData = serializer.fromJson(ctx.body(), UserData.class);
    AuthData output;
    try {
      output = userService.register(userData);
      ctx.status(200);
      String jsonOutput = serializer.toJson(output);
      ctx.result(jsonOutput);
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void login(Context ctx) {
    UserData userData = serializer.fromJson(ctx.body(), UserData.class);
    System.out.println(userData);
    AuthData output;
    try {
      output = userService.login(userData);
      ctx.status(200);
      String jsonOutput = serializer.toJson(output);
      ctx.result(jsonOutput);
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void logout(Context ctx) {
    try {
      String authToken = ctx.header("authorization");
      userService.logout(authToken);
      ctx.status(200);
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void destroy(Context ctx) throws DataAccessException {
    try {
      userService.destroy();
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }
};