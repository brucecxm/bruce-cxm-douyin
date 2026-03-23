package com.bruce.listener;

import com.bruce.config.RabbitMQConfig;
import com.bruce.handler.WebSocketNoteHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(String message) {
        System.out.println("收到RabbitMQ通知消息: " + message);
        // 调用WebSocketNoteHandler发送通知到前端
        WebSocketNoteHandler.sendNotification(message);
    }
}
