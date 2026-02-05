package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bruce.pojo.ChatMessageYi;
import com.bruce.service.FileFeignService;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bruce.config.RabbitConfig.NOTIFY_DELAY_EXCHANGE;
import static com.bruce.config.RabbitConfig.NOTIFY_DELAY_ROUTING_KEY;

/**
 * 聊天控制器 - 简化版本，移除了对不存在的ChatService的依赖
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FileFeignService fileFeignService;

    /**
     * 一对一私聊消息处理
     * 
     * ⭐ STOMP 私聊推送完整原理（核心理解）
     * 
     * 一、使用方式回顾
     * 服务端：
     * template.convertAndSendToUser("1001", "/queue/msg", "hello");
     * 
     * 客户端：
     * client.subscribe("/user/queue/msg", cb);
     * 
     * 注意：Spring 自动拼接映射，客户端写 /user/queue/msg，服务端写 /queue/msg
     */
    @MessageMapping("/private-chat")
    public void privateChat(ChatMessageYi message, Principal principal) {
        System.out.println("当前发消息的是：" + principal.getName());
        System.out.println("准备发送给：" + message.getReceiver());
        System.out.println("消息类型：" + message.getType());

        // 处理文件类型消息（图片/视频）
        if (("image".equals(message.getType()) || "video".equals(message.getType())) && message.getContent() != null) {
            // 前端已经通过HTTP上传文件并获取URL，直接使用即可
            System.out.println("文件消息URL: " + message.getContent());
        }

        // 使用Sa-Token判断接收者是否在线（已登录）
        boolean receiverOnline = StpUtil.isLogin(message.getReceiver());

        // 检查接收者是否处于与当前发送者的聊天界面
        boolean isInChatWithSender = false;
        if (receiverOnline) {
            String redisKey = "user_chat_status:" + message.getReceiver();
            String currentChatUserId = (String) redisTemplate.opsForValue().get(redisKey);
            isInChatWithSender = message.getSender().equals(currentChatUserId);
            System.out.println(
                    "接收者 " + message.getReceiver() + " 是否在与发送者 " + message.getSender() + " 聊天: " + isInChatWithSender);
        }

        // 发送消息给接收者
        if (receiverOnline) {
            if (isInChatWithSender) {
                // 场景1：接收者在线且正在与发送者聊天，直接推送消息
                /**
                 * ⭐ Spring 底层流程：
                 * 1. 查表：找到 user=message.getReceiver() 的所有 session
                 * Spring 内部维护 userId → sessionId 的映射表
                 * 例如：
                 * userId → sessionId
                 * -------------------------
                 * 1001 → s1x9a
                 * 1002 → k22pq
                 * 1001 → z88bc // 同一用户多连接
                 * 
                 * 2. 转换真实地址：
                 * 内部会变成：
                 * /queue/messages-userS1x9a
                 * /queue/messages-userZ88bc
                 * 
                 * 3. 推送：消息发送到对应 WebSocket 连接
                 */
                messagingTemplate.convertAndSendToUser(
                        message.getReceiver(),
                        "/queue/messages",
                        message);
                System.out.println("接收者在线且正在聊天，直接推送消息");
            } else {
                // 场景2：接收者在线但不在与发送者聊天，推送消息并计未读
                /**
                 * ⭐ 多连接支持：
                 * Spring STOMP 自动支持一个用户多个连接
                 * - 多浏览器
                 * - 多标签页
                 * - 多设备
                 * 
                 * 这就是为什么 STOMP 比原生 WebSocket 好用
                 */
                messagingTemplate.convertAndSendToUser(
                        message.getReceiver(),
                        "/queue/messages",
                        message);
                // 增加未读消息计数
                incrementUnreadCount(message.getReceiver(), message.getSender());
                // 触发通知
                triggerNotification(message);
                System.out.println("接收者在线但不在聊天，推送消息并计未读");
            }
        } else {
            // 场景3/4：接收者不在线，暂存消息并发送离线通知（如推送、短信等）
            // 将消息持久化到Redis离线消息队列，待接收者上线后补发
            String offlineKey = "offline_msg:" + message.getReceiver();
            redisTemplate.opsForList().rightPush(offlineKey, message);
            redisTemplate.expire(offlineKey, 7, TimeUnit.DAYS); // 离线消息保留7天

            // 增加未读消息计数
            incrementUnreadCount(message.getReceiver(), message.getSender());

            rabbitTemplate.convertAndSend(
                    NOTIFY_DELAY_EXCHANGE, // 交换机名称（从配置类引用，避免硬编码）
                    NOTIFY_DELAY_ROUTING_KEY, // 路由键
                    message, // 消息内容（支持序列化的对象，如JSON字符串、自定义POJO）
                    msg -> {
                        // 设置延迟时间
                        msg.getMessageProperties().setDelay(5);
                        // 可选：设置消息持久化（避免RabbitMQ重启后消息丢失）
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    });

            System.out.println("接收者离线，消息已暂存Redis并等待补发：" + message.getReceiver());
        }

        // 发送消息给发送者（确保发送者看到的是文件URL而不是Base64）
        boolean senderOnline = StpUtil.isLogin(message.getSender());
        if (senderOnline) {
            /**
             * ⭐ /user 的本质：
             * /user = 用户ID → Session路由层
             * 
             * Spring 帮你维护：
             * - 用户连接关系
             * - 路由转换
             * - 推送执行
             * 
             * 你只关心 userId
             */
            messagingTemplate.convertAndSendToUser(
                    message.getSender(),
                    "/queue/messages",
                    message);
        }
    }

    // 增加未读消息计数
    private void incrementUnreadCount(String userId, String fromUserId) {
        // 使用 Redis 存储未读消息计数
        String redisKey = "unread_count:" + userId + ":" + fromUserId;
        // 原子递增未读消息数
        redisTemplate.opsForValue().increment(redisKey);
        // 设置过期时间，防止内存泄漏
        redisTemplate.expire(redisKey, 7, TimeUnit.DAYS);
        System.out.println("为用户 " + userId + " 增加来自 " + fromUserId + " 的未读消息计数");
    }

    // 触发通知
    private void triggerNotification(ChatMessageYi message) {
        // 这里可以实现推送通知逻辑，例如：
        // 1. 使用 RabbitMQ 发送通知消息
        // 2. 调用第三方推送服务
        // 3. 发送短信通知（适用于重要消息）
        System.out.println("触发通知：用户 " + message.getReceiver() + " 收到来自 " + message.getSender() + " 的消息");
    }

    /**
     * ⭐ 简化的发送消息接口，不依赖ChatService
     * 
     * STOMP SEND流程：
     * 客户端：client.publish({destination:"/app/chat", body:"hello"})
     * 实际Frame：
     * SEND
     * destination:/app/chat
     * 
     * hello
     * 
     * Spring处理：
     * 1. 识别 /app 前缀
     * 2. 路由到 @MessageMapping("/chat")
     * 3. 执行Controller方法
     * 
     * 如果添加 @SendTo("/topic/chat")：
     * Broker会推送MESSAGE到所有订阅者
     * MESSAGE
     * subscription:sub-1
     * destination:/topic/chat
     * 
     * hello
     */
    @MessageMapping("/chat")
    public void handleBasicChat(ChatMessageYi message) {
        System.out.println("收到基础聊天消息: " + message.getContent());
        // 这里只是简单地打印日志，不做实际处理
    }

    // 处理用户进入/离开聊天页面的状态上报
    @MessageMapping("/chat-status")
    public void handleChatStatus(Map<String, Object> statusData, Principal principal) {
        String userId = (String) statusData.get("userId");
        String chatUserId = (String) statusData.get("chatUserId");
        String status = (String) statusData.get("status");

        System.out.println("用户 " + userId + " 聊天状态变更: " + status + "，与 " + chatUserId + " 聊天");

        // 使用 Redis 维护 userId → currentChatUserId 的映射
        String redisKey = "user_chat_status:" + userId;

        if ("enter".equals(status)) {
            // 进入聊天页面，记录当前聊天对象
            redisTemplate.opsForValue().set(redisKey, chatUserId);
            // 设置过期时间，防止内存泄漏（用户可能异常关闭页面）
            redisTemplate.expire(redisKey, 24, TimeUnit.HOURS);

            // 进入聊天页面时，重置未读消息计数
            resetUnreadCount(userId, chatUserId);
        } else if ("leave".equals(status)) {
            // 离开聊天页面，清除映射
            redisTemplate.delete(redisKey);
        }
    }

    // 重置未读消息计数
    private void resetUnreadCount(String userId, String chatUserId) {
        // 使用 Redis 存储未读消息计数
        String redisKey = "unread_count:" + userId + ":" + chatUserId;
        // 删除未读消息计数
        redisTemplate.delete(redisKey);
        System.out.println("重置用户 " + userId + " 来自 " + chatUserId + " 的未读消息计数");
    }

}

//
//
// 好 👍 我们继续往下 —— 这一步就是 **STOMP 架子里最有价值、最容易面试被问的部分**
//
// # ⭐ `/user` 私聊推送完整原理（核心理解）
//
// 你现在已经知道怎么用：
//
// ```java
// convertAndSendToUser(...)
// ```
//
// 但真正重要的是：
//
// > **Spring 是怎么知道消息该发给哪个 WebSocket 连接的？**
//
// 这才是核心。
//
// 我们一步一步拆。
//
// ---
//
// # 一、先看使用方式（回顾）
//
// ## 服务端发送
//
// ```java
// @Autowired
// private SimpMessagingTemplate template;
//
// template.convertAndSendToUser(
// "1001", // 用户ID
// "/queue/msg", // 目标地址
// "hello"
// );
// ```
//
// ---
//
// ## 客户端订阅
//
// ```javascript
// client.subscribe("/user/queue/msg", cb)
// ```
//
// 注意区别：
//
// | 客户端写 | 服务端写 |
// | ----------------- | ------------ |
// | `/user/queue/msg` | `/queue/msg` |
//
// Spring 自动拼接映射
//
// ---
//
// # 二、真正底层流程（非常重要）
//
// 我们按真实执行流程走一遍：
//
// ---
//
// ## ⭐ 第一步：WebSocket 建立连接
//
// 浏览器连接：
//
// ```
// /user/ws
// ```
//
// 进入你写的：
//
// ```
// HandshakeInterceptor
// HandshakeHandler
// ```
//
// 这里你通常做：
//
// ```java
// attributes.put("userId", token解析结果);
// ```
//
// 然后：
//
// ```java
// return new UsernamePasswordAuthenticationToken(userId,null);
// ```
//
// 👉 设置 Principal
//
// ---
//
// # 三、第二步：Spring 建立用户映射表
//
// 连接成功后
// Spring 内部维护一张表（逻辑上类似）
//
// ```
// userId → sessionId
// -------------------------
// 1001 → s1x9a
// 1002 → k22pq
// 1001 → z88bc
// ```
//
// 注意：
//
// # ⭐ 一个用户可以多个连接
//
// * 多浏览器
// * 多标签页
// * 多设备
//
// 这就是为什么 STOMP 比原生好用
//
// ---
//
// # 四、第三步：发送私聊消息
//
// 当你执行：
//
// ```java
// convertAndSendToUser("1001","/queue/msg",data)
// ```
//
// Spring 做了这些事：
//
// ### ① 查表
//
// 找到 user=1001 的所有 session
//
// ```
// s1x9a
// z88bc
// ```
//
// ---
//
// ### ② 转换真实地址
//
// 内部会变成：
//
// ```
// /queue/msg-userS1x9a
// /queue/msg-userZ88bc
// ```
//
// 客户端其实订阅的是这种隐藏地址
//
// 你看不到
//
// ---
//
// ### ③ 推送
//
// 消息发送到对应 WebSocket
//
// ✔ 精准送达
// ✔ 不广播
// ✔ 不遍历你写代码
//
// 全部框架完成
//
// ---
//
// # 五、这就是 `/user` 的本质
//
// 一句话总结：
//
// # ⭐ `/user` = 用户ID → Session路由层
//
// Spring 帮你维护：
//
// * 用户连接关系
// * 路由转换
// * 推送执行
//
// 你只关心 userId
//
// ---
//
// # 六、为什么必须设置 Principal（超级重点）
//
// 如果你不在握手时绑定用户：
//
// ```java
// return null;
// ```
//
// 那么：
//
// ```
// convertAndSendToUser()
// ```
//
// 会失败 ❌
//
// 因为：
//
// Spring 不知道：
//
// > 这个连接属于谁
//
// ---
//
// # 七、面试级总结（你可以直接背）
//
// > Spring STOMP 私聊基于 Principal 与 Session 映射实现。连接建立时通过 HandshakeHandler
// 绑定用户身份，Spring 在内部维护 userId 到 sessionId 的关系表。调用 convertAndSendToUser 时，Broker
// 根据映射将消息路由到对应 session，实现精准推送。
//
// 这回答非常加分。
//
// ---
//
// # 八、进阶一点（真正工程问题）
//
// 这一步你理解后，会马上遇到一个现实问题：
//
// # ❗ 多服务节点怎么办？
//
// ```
// 服务器A 连接
// 服务器B 发消息
// ```
//
// B 根本不知道 session 在 A 上
//
// 私聊失效
//
// ---
//
// ## 企业解决方案
//
// ```
// WebSocket节点
// ↓
// RabbitMQ / Redis PubSub
// ↓
// 跨节点广播
// ```
//
// Spring 支持：
//
// ```
// StompBrokerRelay
// ```
//
// 接入外部 MQ
//
// （这就是生产架构）
//
// ---
//
// # 👉 下一步我建议继续深入（非常关键）
//
// 我们可以继续拆：
//
// ### ⭐ `HandshakeInterceptor` 真正应该写什么
//
// * token校验
// * 登录态绑定
// * 安全设计
//
// 或者
//
// ### ⭐ STOMP 消息完整生命周期
//
// * CONNECT
// * SUBSCRIBE
// * SEND
// * DISCONNECT
//
// 这是抓包级理解
//
// 你选一个方向，我带你继续深入 👍
