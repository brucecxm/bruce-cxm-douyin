package com.bruce.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketNoteHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后，主动发送消息给前端
        session.sendMessage(new TextMessage("Connection established!"));
        System.out.println("WebSocket连接已建立: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 收到消息后响应
        System.out.println("收到来自客户端的消息: " + message.getPayload());
        session.sendMessage(new TextMessage("New notification!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket连接已关闭: " + session.getId());
    }
}
