package client.websocket;

import chess.ChessGame;
import websocket.messages.*;

public interface ServerMessageHandler {
    void handleMessage(ServerMessage serverMessage);

    void updateGame(ChessGame game);
}