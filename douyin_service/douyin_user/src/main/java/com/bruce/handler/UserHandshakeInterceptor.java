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

    /**
     * ⭐ 握手拦截器（安全关键）
     * 
     * 最佳实践：
     * 1. Token校验：从请求中获取token并验证
     * 2. 用户认证：使用认证服务验证用户身份
     * 3. 权限检查：确保用户有权限建立WebSocket连接
     * 4. 身份传递：将用户ID存入attributes，供Handler使用
     * 
     * 生产环境建议：
     * - 从请求头获取token：request.getHeaders().getFirst("Authorization")
     * - 使用JWT等标准认证方案
     * - 集成现有认证框架（如Spring Security、Sa-Token）
     * - 记录连接日志，便于排查问题
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // 【示例：从请求参数获取用户ID】
        // 注意：生产环境应使用token校验，而非直接传递用户ID
        String query = request.getURI().getQuery();
        if (query != null && query.contains("myId=")) {
            String myId = null;
            for (String param : query.split("&")) {
                if (param.startsWith("myId=")) {
                    myId = param.substring("myId=".length());
                    break;
                }
            }
            if (myId != null && !myId.trim().isEmpty()) {
                // ⭐ 关键步骤：将用户ID存入attributes
                // 后续Handler会从这里获取用户ID并设置Principal
                attributes.put("myId", myId);
                return true;
            }
        }

        // 【生产环境推荐实现】
        // String token = request.getHeaders().getFirst("Authorization");
        // if (token != null) {
        //     token = token.replace("Bearer ", "");
        //     // 1. 验证token
        //     Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        //     String userId = claims.getSubject();
        //     // 2. 检查用户是否存在
        //     if (userService.exists(userId)) {
        //         attributes.put("myId", userId);
        //         return true;
        //     }
        // }

        // 没有有效的用户身份，拒绝握手
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手完成后可以做些操作
    }
}
