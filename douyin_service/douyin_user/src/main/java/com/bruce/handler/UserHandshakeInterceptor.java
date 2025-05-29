package com.bruce.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 这里演示从请求参数中获取用户名，绑定到WebSocket会话
 */
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // 假设用户名是通过请求参数传递的 ws://host/ws?username=xxx
        String query = request.getURI().getQuery();
        if (query != null && query.contains("username=")) {
            String username = null;
            for (String param : query.split("&")) {
                if (param.startsWith("username=")) {
                    username = param.substring("username=".length());
                    break;
                }
            }
            if (username != null && !username.trim().isEmpty()) {
                attributes.put("username", username);
                return true;
            }
        }

        // 没有传用户名，拒绝握手
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手完成后可以做些操作
    }
}
