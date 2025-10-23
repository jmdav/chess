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
    javalin.post("/session", userHandler::login);
    javalin.delete("/session", userHandler::logout);
    javalin.get("/game", gameHandler::getList);
    javalin.post("/game", gameHandler::create);
    javalin.put("/game", gameHandler::join);
    javalin.delete("/db", userHandler::destroy);
  }

  public int run(int desiredPort) {
    javalin.start(desiredPort);
    return javalin.port();
  }

  public void stop() { javalin.stop(); }
}
