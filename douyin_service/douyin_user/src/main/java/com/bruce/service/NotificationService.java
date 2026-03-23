package com.bruce.service;

import com.bruce.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    public NotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送通知消息到RabbitMQ队列
     * @param message 通知消息内容
     */
    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, "notification.message", message);
        System.out.println("已发送通知消息到RabbitMQ: " + message);
    }
}
