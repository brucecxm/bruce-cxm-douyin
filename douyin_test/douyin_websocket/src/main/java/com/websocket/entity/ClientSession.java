package com.websocket.entity;

import org.springframework.web.socket.WebSocketSession;

public class ClientSession {
    private final WebSocketSession webSocketSession;
    private final int clientId;

    public ClientSession(WebSocketSession webSocketSession, int clientId) {
        this.webSocketSession = webSocketSession;
        this.clientId = clientId;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "ClientSession{clientId=" + clientId + "}";
    }
}
