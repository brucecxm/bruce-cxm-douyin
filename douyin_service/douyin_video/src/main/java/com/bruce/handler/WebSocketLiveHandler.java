package com.bruce.handler;

import com.bruce.entitys.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/api/live/ws/{channelId}")
@Component
public class WebSocketLiveHandler {

    // 存储所有连接的会话
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    // 存储频道与会话的映射
    private static final Map<String, Map<String, Session>> channelMap = new ConcurrentHashMap<>();

    // 存储频道的观众数量
    private static final Map<String, Integer> viewerCountMap = new ConcurrentHashMap<>();
    // 使用 Spring 管理的 ObjectMapper
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSocketLiveHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("channelId") String channelId) {
        // 原有代码保持不变
        String sessionId = session.getId();
        sessionMap.put(sessionId, session);

        Map<String, Session> channelSessions = channelMap.computeIfAbsent(channelId, k -> new ConcurrentHashMap<>());
        channelSessions.put(sessionId, session);

        int viewerCount = viewerCountMap.compute(channelId, (k, v) -> v == null ? 1 : v + 1);
        broadcastViewerCount(channelId, viewerCount);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("channelId") String channelId) {
        try {
            // 使用Jackson解析JSON字符串为Message对象
            Message msg = objectMapper.readValue(message, Message.class);

            // 根据消息类型处理
            switch (msg.getType()) {
                case "CHAT":
                    broadcastMessage(channelId, message);
                    break;
                case "GIFT":
                    handleGiftMessage(channelId, message);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("channelId") String channelId) {
        // 原有代码保持不变
        String sessionId = session.getId();
        sessionMap.remove(sessionId);

        Map<String, Session> channelSessions = channelMap.get(channelId);
        if (channelSessions != null) {
            channelSessions.remove(sessionId);

            int viewerCount = viewerCountMap.compute(channelId, (k, v) -> v == null ? 0 : Math.max(0, v - 1));
            broadcastViewerCount(channelId, viewerCount);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    // 其他方法保持不变
    private void broadcastMessage(String channelId, String message) {
        Map<String, Session> channelSessions = channelMap.get(channelId);
        if (channelSessions != null) {
            for (Session session : channelSessions.values()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastViewerCount(String channelId, int count) {
        Message viewerCountMsg = new Message();
        viewerCountMsg.setType("VIEWER_COUNT");
        viewerCountMsg.setCount(count);

        try {
            // 使用Jackson将对象转换为JSON字符串
            String jsonMessage = objectMapper.writeValueAsString(viewerCountMsg);
            broadcastMessage(channelId, jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGiftMessage(String channelId, String message) {
        broadcastMessage(channelId, message);
    }
}
