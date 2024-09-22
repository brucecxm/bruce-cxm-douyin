package com.websocket.handle;

import com.websocket.entity.ClientSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//这是基于spring实现websocket
//HttpSessionHandshakeInterceptor（抽象类）：握手拦截器，在握手前后添加操作
//        AbstractWebSocketHandler（抽象类）：WebSocket 处理程序，监听连接前、中、后
//        WebSocketConfigurer（接口）：配置程序，比如配置监听哪个端口，配置自定义的握手拦截器，配置我们自定义的处理程序
@Slf4j
@Component
public class MyWsHandler extends AbstractWebSocketHandler {
    // 使用 ClientSession 替换 SessionBean
    private static Map<String, ClientSession> sessionMap = new ConcurrentHashMap<>();
    private static AtomicInteger clientIdMaker = new AtomicInteger(0);

    // 连接建立
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        // 用 ClientSession 替换 SessionBean
        ClientSession clientSession = new ClientSession(session, clientIdMaker.getAndIncrement());
        sessionMap.put(session.getId(), clientSession);
        log.info(clientSession + " connected");
    }

    // 收到消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        ClientSession clientSession = sessionMap.get(session.getId());
        if (clientSession != null) {
            log.info(clientSession.getClientId() + " : " + message.getPayload());
        }
    }

    // 传输异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        // 如果异常就关闭 session
        if (session.isOpen())
            session.close();
        sessionMap.remove(session.getId());
    }

    // 连接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        ClientSession clientSession = sessionMap.get(session.getId());
        if (clientSession != null) {
            log.info(clientSession + " closed");
            sessionMap.remove(session.getId());
        }
    }

    @Scheduled(fixedRate = 2000) // 每隔2秒执行一次
    public void sendMessage() throws IOException {
        for (String key : sessionMap.keySet()) { // 给所有客户端发送消息
            ClientSession clientSession = sessionMap.get(key);
            if (clientSession != null) {
                clientSession.getWebSocketSession().sendMessage(new TextMessage("beat"));
            }
        }
    }
}
