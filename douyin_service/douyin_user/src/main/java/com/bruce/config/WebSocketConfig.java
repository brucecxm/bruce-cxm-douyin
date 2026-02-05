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
 * 
 * ⭐ STOMP 消息完整生命周期
 * 
 * 总览：
 * 1️⃣ CONNECT —— 建立STOMP会话
 * 2️⃣ CONNECTED —— 服务端确认
 * 3️⃣ SUBSCRIBE —— 订阅消息
 * 4️⃣ SEND —— 发送消息
 * 5️⃣ MESSAGE —— 接收推送
 * 6️⃣ DISCONNECT —— 断开连接
 * 
 * 完整层级：
 * TCP
 * └─ WebSocket
 * └─ STOMP
 * 
 * 面试加分点 ⭐
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 消息流程图：
     * 浏览器(stomp.js)
     * │
     * ▼
     * WebSocket连接端点
     * (registerStompEndpoints)
     * │
     * ▼
     * Spring Message Broker
     * (SimpleBroker / RabbitMQ)
     * │
     * ├──── /app → Controller处理
     * │
     * └──── /topic /queue → 推送客户端
     */

    /**
     * ⭐ STOMP 消息完整生命周期详解
     * 
     * 一、① CONNECT（客户端发起）
     * 前端执行：client.activate()
     * 实际发送Frame：
     * CONNECT
     * accept-version:1.2
     * heart-beat:10000,10000
     * 
     * \0
     * 
     * 注意：WebSocket连接已建立，这是STOMP层建立会话
     * 
     * 二、② CONNECTED（服务端响应）
     * Spring Broker回复：
     * CONNECTED
     * version:1.2
     * heart-beat:0,0
     * 
     * \0
     * 
     * 此时：STOMP Session创建完成
     * 
     * 三、③ SUBSCRIBE（客户端订阅）
     * 前端：client.subscribe("/topic/chat")
     * 实际发送：
     * SUBSCRIBE
     * id:sub-1
     * destination:/topic/chat
     * 
     * Broker内部记录：sessionX → /topic/chat
     * 
     * 四、④ SEND（客户端发送）
     * 前端：client.publish({destination:"/app/chat", body:"hello"})
     * 实际Frame：
     * SEND
     * destination:/app/chat
     * 
     * hello
     * 
     * 进入：@MessageMapping("/chat")
     * 
     * 五、⑤ MESSAGE（Broker推送）
     * Controller：@SendTo("/topic/chat")
     * Broker推送：
     * MESSAGE
     * subscription:sub-1
     * destination:/topic/chat
     * 
     * hello
     * 
     * 六、⑥ DISCONNECT（关闭连接）
     * 前端：client.deactivate() 或浏览器关闭
     * 发送：DISCONNECT
     * 
     * Spring：删除订阅、删除session、清理user映射
     * 
     * ⭐ 完整真实流程（工程级理解）
     * ① TCP连接 → ② WebSocket握手 → ③ STOMP CONNECT → ④ 服务端CONNECTED →
     * ⑤ 客户端SUBSCRIBE → ⑥ 客户端SEND → ⑦ Broker MESSAGE推送 → ⑧ DISCONNECT
     * 
     * ⭐ 面试杀手点
     * 此生命周期解释：
     * - 心跳为什么存在
     * - 重连怎么实现
     * - 订阅为什么会丢
     * - 多节点为什么要MQ
     * - `/user`为什么可路由
     * 
     * ⭐ 面试总结
     * STOMP在WebSocket之上建立会话，其生命周期包括CONNECT建立协议连接、SUBSCRIBE注册订阅、
     * SEND发送消息、Broker触发MESSAGE推送以及DISCONNECT释放资源。
     * Spring WebSocket利用该模型实现发布订阅通信。
     */

    /**
     * ⭐ 连接建立流程（非常重要）
     * 浏览器请求连接：/user/ws
     * ↓
     * HandshakeInterceptor
     * - 校验token
     * - 取用户ID
     * - 存入attributes
     * ↓
     * HandshakeHandler
     * - 设置Principal（关键）
     * - 绑定用户身份
     * - return new UsernamePasswordAuthenticationToken(userId, null);
     * ↓
     * 建立连接成功
     * 
     * 👉 为什么必须设置 Principal？
     * 如果不在握手时绑定用户：return null;
     * 那么：convertAndSendToUser() 会失败 ❌
     * 因为：Spring 不知道这个连接属于谁
     */

    /**
     * 二、第一层 —— 连接入口（Endpoint）
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WsConstant.user + WsConstant.ws) // 客户端连接的端点
                .setHandshakeHandler(new UserHandshakeHandler()) // 使用自定义 HandshakeHandler
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
         * destination: "/app/chat"
         * });
         * 
         * 消息不会直接广播，而是：
         * 1. 进入 Controller
         * 2. Controller处理：
         * @MessageMapping("/chat")
         * @SendTo("/topic/msg")
         * public String chat(String msg) {
         * return msg;
         * }
         * 3. 流程：
         * 客户端 → /app/chat → Controller → 转发 → /topic/msg → 所有订阅者收到
         */

        // 应用前缀，客户端发送消息的路径要以 /app 开头
        registry.setApplicationDestinationPrefixes(WsConstant.app);

        /**
         * ⭐ 五、第四层 —— 用户私聊路由（重点）
         * 
         * 配置：
         * registry.setUserDestinationPrefix("/user");
         * 
         * 支持功能：
         * ⭐ 私聊推送（发送给某个用户）
         * simpMessagingTemplate.convertAndSendToUser(
         * userId,
         * "/queue/msg",
         * data
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
         * ⭐ Spring 建立用户映射表
         * 连接成功后，Spring 内部维护一张表（逻辑上类似）：
         * userId → sessionId
         * -------------------------
         * 1001 → s1x9a
         * 1002 → k22pq
         * 1001 → z88bc // 同一用户多连接
         * 
         * 注意：一个用户可以多个连接
         * - 多浏览器
         * - 多标签页
         * - 多设备
         * 
         * ⭐ 发送私聊消息流程
         * 当你执行：convertAndSendToUser("1001", "/queue/msg", data)
         * Spring 做了这些事：
         * 1. 查表：找到 user=1001 的所有 session
         * s1x9a
         * z88bc
         * 
         * 2. 转换真实地址：
         * 内部会变成：
         * /queue/msg-userS1x9a
         * /queue/msg-userZ88bc
         * 
         * 3. 推送：消息发送到对应 WebSocket 连接
         * ✔ 精准送达
         * ✔ 不广播
         * ✔ 不遍历你写代码
         * 全部框架完成
         * 
         * ⭐ 面试级总结
         * Spring STOMP 私聊基于 Principal 与 Session 映射实现。连接建立时通过 HandshakeHandler 绑定用户身份，
         * Spring 在内部维护 userId 到 sessionId 的关系表。调用 convertAndSendToUser 时，
         * Broker 根据映射将消息路由到对应 session，实现精准推送。
         * 
         * ⭐ 多服务节点方案
         * 企业级解决方案：
         * WebSocket节点 → RabbitMQ / Redis PubSub → 跨节点广播
         * 
         * Spring 支持：StompBrokerRelay
         * 接入外部 MQ（这就是生产架构）
         * 
         * 👉 这是 STOMP 最强功能之一
         */

        // 点对点时必须配置
        registry.setUserDestinationPrefix(WsConstant.user);
    }

}
