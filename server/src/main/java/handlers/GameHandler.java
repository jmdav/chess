package handlers;
import com.google.gson.Gson;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameID;
import model.GameList;
import model.GameRequestData;
import model.GameStartData;
import service.GameService;

public class GameHandler {

  Gson serializer = new Gson();

  private final GameService gameService;

  public GameHandler(GameService gameService, AuthDataAccess authDataAccess) {
    this.gameService = gameService;
  }

  public void listGames(Context ctx) {
    String authToken = ctx.header("authorization");
    GameList output;
    try {
      output = gameService.listGames(authToken);
      ctx.status(200);
      String jsonOutput = serializer.toJson(output);
      ctx.result(jsonOutput);
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void createGame(Context ctx) {
    String authToken = ctx.header("authorization");
    String gameName =
        serializer.fromJson(ctx.body(), GameStartData.class).gameName();
    GameID output;
    try {
      output = gameService.createGame(authToken, gameName);
      ctx.status(200);
      ctx.result(serializer.toJson(output));
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void joinGame(Context ctx) {
    String authToken = ctx.header("authorization");
    GameRequestData gameRequest =
        serializer.fromJson(ctx.body(), GameRequestData.class);
    try {
      gameService.joinGame(authToken, gameRequest);
      ctx.status(200);
    } catch (DataAccessException e) {
      ctx.status(e.getStatusCode());
      ctx.result(serializer.toJson(e.getErrorMessage()));
    }
  }

  public void destroy(Context ctx) throws DataAccessException {
    gameService.destroy();
  }
}
