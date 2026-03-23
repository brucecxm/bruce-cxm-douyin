package com.bruce.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketNoteHandler extends TextWebSocketHandler {

    // 存储所有活跃的WebSocket会话
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 添加会话到集合
        sessions.add(session);
        // 连接建立后，主动发送消息给前端
//        session.sendMessage(new TextMessage("Connection established!"));
        System.out.println("WebSocket连接已建立: " + session.getId());
        System.out.println("当前活跃连接数: " + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 收到消息后响应
        System.out.println("收到来自客户端的消息: " + message.getPayload());
//        session.sendMessage(new TextMessage("New notification!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从集合中移除会话
        sessions.remove(session);
        System.out.println("WebSocket连接已关闭: " + session.getId());
        System.out.println("当前活跃连接数: " + sessions.size());
    }

    // 发送通知给所有连接的客户端
    public static void sendNotification(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                    System.out.println("已发送通知到会话: " + session.getId());
                } catch (Exception e) {
                    System.err.println("发送通知失败: " + e.getMessage());
                }
            }
        }
    }
}
