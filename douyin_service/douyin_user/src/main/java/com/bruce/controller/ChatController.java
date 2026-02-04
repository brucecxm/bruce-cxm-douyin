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

        // 发送消息给接收者
        if (receiverOnline) {
            // 场景1/2：接收者在线，直接推送消息
            messagingTemplate.convertAndSendToUser(
                    message.getReceiver(),
                    "/queue/messages",
                    message);
        } else {
            // 场景3/4：接收者不在线，暂存消息并发送离线通知（如推送、短信等）
            // 将消息持久化到Redis离线消息队列，待接收者上线后补发
            String offlineKey = "offline_msg:" + message.getReceiver();
            redisTemplate.opsForList().rightPush(offlineKey, message);
            redisTemplate.expire(offlineKey, 7, TimeUnit.DAYS); // 离线消息保留7天

            rabbitTemplate.convertAndSend(
                    NOTIFY_DELAY_EXCHANGE,  // 交换机名称（从配置类引用，避免硬编码）
                    NOTIFY_DELAY_ROUTING_KEY,  // 路由键
                    message,  // 消息内容（支持序列化的对象，如JSON字符串、自定义POJO）
                    msg -> {
                        // 设置延迟时间
                        msg.getMessageProperties().setDelay(5);
                        // 可选：设置消息持久化（避免RabbitMQ重启后消息丢失）
                        msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return msg;
                    }
            );

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

    // 简化的发送消息接口，不依赖ChatService
    @MessageMapping("/chat")
    public void handleBasicChat(ChatMessageYi message) {
        System.out.println("收到基础聊天消息: " + message.getContent());
        // 这里只是简单地打印日志，不做实际处理
    }


}
