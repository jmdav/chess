package server;
import model.*;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
import io.javalin.*;

public class Server {

    private final ChessService service;
    private final WebSocketHandler webSocketHandler;
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/pet", this::addPet)
        ;

        // Register your endpoints and exception handlers here.


    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.json(ex.toJson());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void addPet(Context ctx) throws ResponseException {
        Pet pet = new Gson().fromJson(ctx.body(), Pet.class);
        pet = service.addPet(pet);
        webSocketHandler.makeNoise(pet.name(), pet.sound());
        ctx.json(new Gson().toJson(pet));
    }

}
