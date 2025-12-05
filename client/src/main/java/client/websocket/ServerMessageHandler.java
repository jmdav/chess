package client.websocket;

import websocket.messages.*;

public interface ServerMessageHandler {
    void handleMessage(ServerMessage serverMessage);
}