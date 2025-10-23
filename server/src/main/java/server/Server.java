package server;

import dataAccess.AuthRAMDAO;
import handlers.*;
import io.javalin.*;
import service.*;

public class Server {

  private final Javalin javalin;

  public Server() {

    AuthRAMDAO authDB = new AuthRAMDAO();
    UserService userService = new UserService(authDB);
    UserHandler userHandler = new UserHandler(userService, authDB);

    GameService gameService = new GameService(authDB);
    GameHandler gameHandler = new GameHandler(gameService, authDB);

    javalin = Javalin.create(config -> config.staticFiles.add("web"));
    javalin.post("/user", userHandler::register);
    javalin.post("/session", userHandler::login);
    javalin.delete("/session", userHandler::logout);
    javalin.get("/game", gameHandler::listGames);
    javalin.post("/game", gameHandler::createGame);
    javalin.put("/game", gameHandler::joinGame);
    javalin.delete("/db", context -> {
      userHandler.destroy(context);
      gameHandler.destroy(context);
    });
  }

  public int run(int desiredPort) {
    javalin.start(desiredPort);
    return javalin.port();
  }

  public void stop() { javalin.stop(); }
}
