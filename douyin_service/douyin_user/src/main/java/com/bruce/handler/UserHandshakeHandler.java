package com.bruce.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义 HandshakeHandler，把之前放入的用户名包装成 Principal
 * 
 * ⭐ STOMP Session 创建与 Principal 关系
 * 
 * WebSocket连接 → Handshake → STOMP Session创建流程：
 * 1. TCP连接建立
 * 2. WebSocket握手（HTTP Upgrade）
 * 3. HandshakeInterceptor 执行（token校验）
 * 4. HandshakeHandler.determineUser() 执行（设置Principal）
 * 5. WebSocket Session创建
 * 6. 客户端发送 STOMP CONNECT
 * 7. 服务端回复 STOMP CONNECTED
 * 8. ⭐ STOMP Session创建完成
 * 
 * 关键点：
 * - Principal是STOMP Session的身份标识
 * - 没有Principal，STOMP私聊推送会失败
 * - Principal绑定了用户ID与WebSocket Session的关系
 */
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * ⭐ 设置 Principal（超级重点）
     * 
     * 为什么这一步至关重要？
     * 1. 身份绑定：将用户ID与WebSocket连接绑定
     * 2. 用户映射：Spring 基于Principal维护userId → sessionId映射表
     * 3. 私聊基础：convertAndSendToUser() 依赖此映射表
     * 4. 多连接支持：同一用户多设备/多标签页的基础
     * 
     * 如果返回null：
     * - convertAndSendToUser() 会失败
     * - 无法实现私聊功能
     * - Spring 不知道连接属于谁
     * 
     * 生产环境建议：
     * return new UsernamePasswordAuthenticationToken(userId, null);
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String myId = (String) attributes.get("myId");
        // 返回 Principal 对象，Spring 会用它做用户区分
        // 这是 STOMP 私聊推送的核心基础
        return new StompPrincipal(myId);
    }

    private static class StompPrincipal implements Principal {
        private final String name;

        public StompPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
