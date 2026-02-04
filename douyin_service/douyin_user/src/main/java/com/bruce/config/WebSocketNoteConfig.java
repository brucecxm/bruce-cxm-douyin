package com.bruce.config;

import com.bruce.dict.WsConstant;
import com.bruce.handler.WebSocketNoteHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketNoteConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketNoteHandler(), WsConstant.user+WsConstant.wsnote)
                .setAllowedOriginPatterns("*");
    }
}

