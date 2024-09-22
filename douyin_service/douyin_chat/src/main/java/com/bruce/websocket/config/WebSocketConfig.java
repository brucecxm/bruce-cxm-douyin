package com.bruce.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置消息代理
        // 启用简单的消息代理，处理以 /topic 开头的目的地
        config.enableSimpleBroker("/topic");
        // 配置应用程序的目的地前缀，处理以 /app 开头的目的地
        config.setApplicationDestinationPrefixes("/app");
    }




    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 STOMP 端点
        // /chat 端点用于连接 WebSocket
        // 允许的跨域来源设置为 http://localhost:5500 和 http://192.168.1.100:8081
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173", "http://192.168.1.100:8081") // 允许的跨域来源
                .withSockJS(); // 启用 SockJS 支持，以便在 WebSocket 不被支持的情况下回退到其他传输方式
    }
}
