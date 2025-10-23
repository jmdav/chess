package handlers;
import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import io.javalin.http.Context;
import java.util.List;
import model.AuthData;
import model.GameData;
import model.GameRequestData;
import model.GameStartData;
import model.UserData;
import service.GameService;

public class GameHandler {

  Gson serializer = new Gson();

  private final GameService gameService;

  public GameHandler(GameService gameService, AuthDataAccess authDataAccess) {
    this.gameService = gameService;
  }

  public void listGames(Context ctx) {
    String authToken = ctx.header("authorization");
    List<GameData> output;
    try {
      output = gameService.listGames(authToken);
      ctx.status(201);
      String jsonOutput = serializer.toJson(output);
      ctx.result(jsonOutput);
    } catch (DataAccessException e) {
      ctx.status(400);
    }
  }

  public void createGame(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    String gameName =
        serializer.fromJson(ctx.body(), GameStartData.class).gameName();
    String output = gameService.createGame(authToken, gameName);
    ctx.status(201);
    String jsonOutput = serializer.toJson(output);
    ctx.result(jsonOutput);
  }

  public void joinGame(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    GameRequestData gameRequest =
        serializer.fromJson(ctx.body(), GameRequestData.class);
    gameService.joinGame(authToken, gameRequest);
    ctx.status(200);
  }

  public void destroy(Context ctx) throws DataAccessException {
    gameService.destroy();
  }
}
