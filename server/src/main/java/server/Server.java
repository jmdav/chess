package server;

import handlers.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.*;

public class Server {

  private final Javalin javalin;

  public Server() {

    UserService userService = new UserService();
    UserHandler userHandler = new UserHandler(userService);

    javalin = Javalin.create(config -> config.staticFiles.add("web"));
    javalin.get("/user", userHandler::createUser);
  }

  private void exceptionHandler(ResponseException ex, Context ctx) {
    ctx.status(ex.toHttpStatusCode());
    ctx.json(ex.toJson());
  }

  public int run(int desiredPort) {
    javalin.start(desiredPort);
    return javalin.port();
  }

  public void stop() { javalin.stop(); }
}
