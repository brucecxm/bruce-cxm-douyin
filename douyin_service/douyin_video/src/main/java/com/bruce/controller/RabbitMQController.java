package com.bruce.controller;

import com.bruce.dto.LikeMessage;
import com.bruce.pojo.Result;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送简单消息
     * 
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return 响应结果
     */
    @PostMapping("/send/simple")
    public Result<Map<String, Object>> sendSimpleMessage(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam String message) {

        Map<String, Object> response = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            response.put("code", 200);
            response.put("message", "消息发送成功");
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "消息发送失败: " + e.getMessage());
            return Result.success(response);
        }
    }

    /**
     * 发送持久化消息
     * 
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return 响应结果
     */
    @PostMapping("/send/persistent")
    public Result<Map<String, Object>> sendPersistentMessage(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam String message) {

        Map<String, Object> response = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return msg;
            });
            response.put("code", 200);
            response.put("message", "持久化消息发送成功");
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "持久化消息发送失败: " + e.getMessage());
            return Result.success(response);
        }
    }

    /**
     * 发送对象消息
     * 
     * @param exchange    交换机名称
     * @param routingKey  路由键
     * @param likeMessage 点赞消息对象
     * @return 响应结果
     */
    @PostMapping("/send/object")
    public Result<Map<String, Object>> sendObjectMessage(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestBody LikeMessage likeMessage) {

        Map<String, Object> response = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, likeMessage);
            response.put("code", 200);
            response.put("message", "对象消息发送成功");
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "对象消息发送失败: " + e.getMessage());
            return Result.success(response);
        }
    }

    /**
     * 发送带回调的消息
     * 
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return 响应结果
     */
    @PostMapping("/send/with-callback")
    public Result<Map<String, Object>> sendMessageWithCallback(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam String message) {

        Map<String, Object> response = new HashMap<>();
        try {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
            response.put("code", 200);
            response.put("message", "带回调消息发送成功");
            response.put("correlationId", correlationData.getId());
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "带回调消息发送失败: " + e.getMessage());
            return Result.success(response);
        }
    }

    /**
     * 发送批量消息
     * 
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param messages   消息列表
     * @return 响应结果
     */
    @PostMapping("/send/batch")
    public Result<Map<String, Object>> sendBatchMessages(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestBody String[] messages) {

        Map<String, Object> response = new HashMap<>();
        try {
            for (String msg : messages) {
                rabbitTemplate.convertAndSend(exchange, routingKey, msg);
            }
            response.put("code", 200);
            response.put("message", "批量消息发送成功");
            response.put("count", messages.length);
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "批量消息发送失败: " + e.getMessage());
            return Result.success(response);
        }
    }

    /**
     * 获取RabbitMQ连接状态
     * 
     * @return 响应结果
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> getRabbitMQStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查连接是否正常
            rabbitTemplate.getConnectionFactory().createConnection().close();
            response.put("code", 200);
            response.put("message", "RabbitMQ连接正常");
            return Result.success(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "RabbitMQ连接异常: " + e.getMessage());
            return Result.success(response);
        }
    }
}