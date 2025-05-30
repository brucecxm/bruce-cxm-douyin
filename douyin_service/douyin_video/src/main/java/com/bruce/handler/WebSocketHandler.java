package com.bruce.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// WebSocketHandler 继承自 TextWebSocketHandler，专门处理 WebSocket 消息
public class WebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();  // 用于 JSON 序列化和反序列化

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("WebSocket连接建立，当前连接数：" + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 将收到的弹幕消息转发给所有连接的 WebSocket 会话
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                // 直接发送文本消息（JSON 格式）
                webSocketSession.sendMessage(message);
            }
        }
        System.out.println("转发消息：" + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("WebSocket连接关闭，当前连接数：" + sessions.size());
    }
}
