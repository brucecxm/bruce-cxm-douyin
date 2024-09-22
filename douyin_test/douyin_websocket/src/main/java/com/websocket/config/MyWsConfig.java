package com.websocket.config;

import com.websocket.handle.MyInterceptor;
import com.websocket.handle.MyWsHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
@Configuration
@EnableWebSocket    // 启用 spring 提供的 websocket 功能
public class MyWsConfig implements WebSocketConfigurer {

    @Resource
    MyWsHandler myWsHandler; // 引入我们在MyWsHandler上声明的Bean(@Component)
    @Resource
    MyInterceptor myInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 监听的地址
        registry.addHandler(myWsHandler,"/myWs1")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*"); // 允许的源
    }
}