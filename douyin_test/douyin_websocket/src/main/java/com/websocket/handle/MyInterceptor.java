package com.websocket.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//HttpSessionHandshakeInterceptor（抽象类）：握手拦截器，在握手前后添加操作
//        AbstractWebSocketHandler（抽象类）：WebSocket 处理程序，监听连接前、中、后
//        WebSocketConfigurer（接口）：配置程序，比如配置监听哪个端口，配置自定义的握手拦截器，配置我们自定义的处理程序



import java.util.Map;

/**
 * WebSocket 自定义握手拦截器
 * 这个拦截器在 WebSocket 握手过程中进行自定义处理。
 * 继承自 HttpSessionHandshakeInterceptor，重写了握手前后的处理方法。
 */
@Component
@Slf4j
public class MyInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 在 WebSocket 握手前调用的方法。
     * 可以在这个方法中添加自定义的逻辑，例如记录请求的远程地址。
     *
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param wsHandler 当前的 WebSocket 处理程序
     * @param attributes 用于存储握手过程中传递的属性
     * @return 返回 true 允许握手继续，返回 false 则终止握手
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手前"); // 记录握手前的信息
        log.info("远程地址 => " + request.getRemoteAddress()); // 记录远程地址
        // 调用父类的 beforeHandshake 方法，保留父类的操作
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /**
     * 在 WebSocket 握手完成后调用的方法。
     * 可以在这个方法中添加完成握手后的自定义逻辑。
     *
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param wsHandler 当前的 WebSocket 处理程序
     * @param ex 握手过程中可能抛出的异常
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("完成握手"); // 记录握手完成的信息
        // 调用父类的 afterHandshake 方法，保留父类的操作
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
