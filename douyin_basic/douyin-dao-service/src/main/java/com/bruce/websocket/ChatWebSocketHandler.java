package com.bruce.websocket;

import org.springframework.web.socket.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 存储当前所有 WebSocket 连接的用户
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        sessions.put(userId, session);
        System.out.println("WebSocket connected for user: " + userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息
        String payload = message.getPayload();
        Long senderId = (Long) session.getAttributes().get("userId");

        // 解析消息，判断是个人消息还是群聊消息
        // 推送消息到目标客户端或者群聊
        System.out.println("Received message: " + payload);

        // 假设直接将消息发送给所有连接的客户端
        // 发送给所有已连接用户（广播消息）
        for (Map.Entry<Long, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession userSession = entry.getValue();
            if (userSession.isOpen()) {
                userSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        sessions.remove(userId);
        System.out.println("WebSocket connection closed for user: " + userId);
    }

    public void sendMessageToUser(Long userId, String message) throws Exception {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public void broadcastMessage(String message) throws Exception {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

}
