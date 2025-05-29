package com.bruce.config;

import com.bruce.handler.UserHandshakeHandler;
import com.bruce.handler.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 开启两个broker：一个是群聊用的 /topic，一个是一对一用的 /queue
        registry.enableSimpleBroker("/topic", "/queue");

        // 应用前缀，客户端发送消息的路径要以 /app 开头
        registry.setApplicationDestinationPrefixes("/app");

        // 点对点时必须配置
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/user/ws") // 客户端连接的端点
                .setHandshakeHandler(new UserHandshakeHandler())   // 使用自定义 HandshakeHandler
                .addInterceptors(new UserHandshakeInterceptor()) // 绑定用户身份
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
