package com.bruce.config;

import com.bruce.dict.WsConstant;
import com.bruce.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// @Configuration 注解表示该类是一个配置类
@Configuration
// @EnableWebSocket 注解启用 WebSocket 配置
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    // 重写 registerWebSocketHandlers 方法来注册 WebSocket 处理器
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 将 WebSocket 请求路径设置为 "/video/ws"，以便匹配前端的 WebSocket 请求路径
        registry.addHandler(new WebSocketHandler(), WsConstant.video+WsConstant.ws)
                // setAllowedOrigins("*") 设置允许的跨域请求来源，这里设置为 "*" 表示允许所有来源
                .setAllowedOrigins("*");
    }
}
