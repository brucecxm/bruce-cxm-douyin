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

    // 一对一私聊消息处理
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
                messagingTemplate.convertAndSendToUser(
                        message.getReceiver(),
                        "/queue/messages",
                        message);
                System.out.println("接收者在线且正在聊天，直接推送消息");
            } else {
                // 场景2：接收者在线但不在与发送者聊天，推送消息并计未读
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

    // 简化的发送消息接口，不依赖ChatService
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
