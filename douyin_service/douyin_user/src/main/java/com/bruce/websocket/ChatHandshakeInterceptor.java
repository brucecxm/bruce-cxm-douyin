//package com.bruce.websocket;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//import org.springframework.web.socket.WebSocketSession;
//
//import javax.websocket.HandshakeResponse;
//import javax.websocket.server.HandshakeRequest;
//import java.util.Map;
//
//public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
//    @Override
//    public boolean beforeHandshake(HandshakeRequest request, HandshakeResponse response) throws Exception {
//        Map<String, String[]> parameters = request.getParameterMap();
//        String userIdParam = parameters.get("userId")[0];
//        Long userId = Long.parseLong(userIdParam);
//
//        // 在 WebSocket 会话中存储用户信息
//        request.getAttributes().put("userId", userId);
//        return super.beforeHandshake(request, response);
//    }
//}