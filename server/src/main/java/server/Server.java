package server;

import handlers.*;
import io.javalin.*;
import service.*;

public class Server {

  private final Javalin javalin;

  public Server() {

    UserService userService = new UserService();
    UserHandler userHandler = new UserHandler(userService);

    javalin = Javalin.create(config -> config.staticFiles.add("web"));
    javalin.post("/user", userHandler::register);
  }

  public int run(int desiredPort) {
    javalin.start(desiredPort);
    return javalin.port();
  }

  public void stop() { javalin.stop(); }
}
