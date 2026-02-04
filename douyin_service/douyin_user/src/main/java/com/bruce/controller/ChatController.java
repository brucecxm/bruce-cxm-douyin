package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bruce.pojo.ChatMessageYi;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
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

    // 文件存储路径
    private static final String STORAGE_PATH = "D:\\chat_files";

    // 一对一私聊消息处理
    @MessageMapping("/private-chat")
    public void privateChat(ChatMessageYi message, Principal principal) {
        System.out.println("当前发消息的是：" + principal.getName());
        System.out.println("准备发送给：" + message.getReceiver());

        // 处理文件上传
        if (message.getType() != null && ("image".equals(message.getType()) || "video".equals(message.getType()))) {
            try {
                // 解码Base64内容并保存文件
                String fileUrl = saveFile(message);
                message.setFileUrl(fileUrl);
                // 清空Base64内容，只保留文件URL
                message.setContent(fileUrl);
            } catch (Exception e) {
                System.error("文件处理失败: " + e.getMessage());
                // 处理失败，发送错误消息
                message.setType("text");
                message.setContent("文件上传失败，请重试");
            }
        }

        // 使用Sa-Token判断接收者是否在线（已登录）
        boolean receiverOnline = StpUtil.isLogin(message.getReceiver());

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
    }

    // 保存文件
    private String saveFile(ChatMessageYi message) throws IOException {
        // 创建存储目录
        File storageDir = new File(STORAGE_PATH);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // 创建子目录（按类型）
        String typeDir = message.getType().equals("image") ? "images" : "videos";
        File typeStorageDir = new File(storageDir, typeDir);
        if (!typeStorageDir.exists()) {
            typeStorageDir.mkdirs();
        }

        // 生成文件名
        String fileName = System.currentTimeMillis() + "_" + message.getFileName();
        File file = new File(typeStorageDir, fileName);

        // 解码Base64内容
        if (message.getContent() != null && message.getContent().contains(",")) {
            String base64Content = message.getContent().split(",")[1];
            byte[] bytes = Base64.getDecoder().decode(base64Content);

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }
        }

        // 返回文件URL（这里使用本地文件路径，实际项目中应使用HTTP URL）
        return "file://" + file.getAbsolutePath();
    }

    // 简化的发送消息接口，不依赖ChatService
    @MessageMapping("/chat")
    public void handleBasicChat(ChatMessageYi message) {
        System.out.println("收到基础聊天消息: " + message.getContent());
        // 这里只是简单地打印日志，不做实际处理
    }
}
