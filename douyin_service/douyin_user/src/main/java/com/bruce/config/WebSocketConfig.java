package com.bruce.config;

import com.bruce.dict.WsConstant;
import com.bruce.handler.UserHandshakeHandler;
import com.bruce.handler.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


/**
 * 六、前端完整最小连接代码
 * 你项目可以直接用
 */
// const client = new Client({
//     webSocketFactory: () => new SockJS("/user/ws")
// });
// 
// client.onConnect = () => {
//     // 群聊
//     client.subscribe("/topic/msg", m => {
//         console.log(m.body);
//     });
//     
//     // 私聊
//     client.subscribe("/user/queue/msg", m => {
//         console.log(m.body);
//     });
//     
//     // 发消息
//     client.publish({
//         destination: "/app/chat",
//         body: "hello"
//     });
// };
// 
// client.activate();


/**
 * 重要说明：
 * 👉 这不是原生 WebSocket 架子
 * 👉 这是 STOMP（消息代理模式）的架子
 */


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 消息流程图：
     *              浏览器(stomp.js)
     *                  │
     *                  ▼
     *              WebSocket连接端点
     *              (registerStompEndpoints)
     *                  │
     *                  ▼
     *              Spring Message Broker
     *              (SimpleBroker / RabbitMQ)
     *                  │
     *                  ├──── /app → Controller处理
     *                  │
     *                  └──── /topic /queue → 推送客户端
     */

    /**
     * 连接建立流程：
     *     浏览器请求连接
     *         ↓
     *     HandshakeInterceptor
     *         ↓
     *     HandshakeHandler
     *         ↓
     *     建立连接成功
     * 
     *     ***Interceptor常做***
     *         - 校验token
     *         - 取用户ID
     *         - 存入attributes
     *     
     *     ***Handler常做***
     *         - 设置Principal（关键）
     *         - 绑定用户身份
     *         👉 这是后面 /user/xxx 私聊的基础
     */

    /**
     * 二、第一层 —— 连接入口（Endpoint）
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WsConstant.user + WsConstant.ws) // 客户端连接的端点
                .setHandshakeHandler(new UserHandshakeHandler())   // 使用自定义 HandshakeHandler
                .addInterceptors(new UserHandshakeInterceptor()) // 绑定用户身份
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


    /**
     * 三、第二层 —— Broker（消息代理）
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * 核心配置：
         * registry.enableSimpleBroker("/topic", "/queue");
         * 这一步非常关键，它启动：
         * ⭐ 内置消息服务器
         * 就像一个小型 MQ，负责：
         * - 保存订阅关系
         * - 路由消息
         * - 推送客户端
         * 
         * 两种常见地址：
         * - 群发：/topic/chat（订阅者全部收到）
         * - 点对点：/queue/msg（只发给一个用户）
         */

        // 开启两个broker：一个是群聊用的 /topic，一个是一对一用的 /queue
        registry.enableSimpleBroker(WsConstant.topic, WsConstant.queue);

        /**
         * 客户端发送消息流程：
         * client.publish({
         *     destination: "/app/chat"
         * });
         * 
         * 消息不会直接广播，而是：
         * 1. 进入 Controller
         * 2. Controller处理：
         *    @MessageMapping("/chat")
         *    @SendTo("/topic/msg")
         *    public String chat(String msg) {
         *        return msg;
         *    }
         * 3. 流程：
         *    客户端 → /app/chat → Controller → 转发 → /topic/msg → 所有订阅者收到
         */

        // 应用前缀，客户端发送消息的路径要以 /app 开头
        registry.setApplicationDestinationPrefixes(WsConstant.app);

        /**
         * 五、第四层 —— 用户私聊路由（重点）
         * 
         * 配置：
         * registry.setUserDestinationPrefix("/user");
         * 
         * 支持功能：
         * ⭐ 私聊推送（发送给某个用户）
         * simpMessagingTemplate.convertAndSendToUser(
         *     userId,
         *     "/queue/msg",
         *     data
         * );
         * 
         * 客户端订阅：
         * client.subscribe("/user/queue/msg", ...);
         * 
         * 注意：
         * - 客户端写：/user/queue/msg
         * - 服务器写：/queue/msg
         * Spring 自动映射
         * 
         * 👉 这是 STOMP 最强功能之一
         */

        // 点对点时必须配置
        registry.setUserDestinationPrefix(WsConstant.user);
    }

}
