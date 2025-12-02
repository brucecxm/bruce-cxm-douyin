package com.bruce.controller;

import com.bruce.dto.LikeMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "RabbitMQ操作接口")
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
    @ApiOperation(value = "发送简单消息", notes = "向指定交换机和路由键发送简单文本消息")
    @PostMapping("/send/simple")
    public ResponseEntity<Map<String, Object>> sendSimpleMessage(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam String message) {

        Map<String, Object> response = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            response.put("code", 200);
            response.put("message", "消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
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
    @ApiOperation(value = "发送持久化消息", notes = "向指定交换机和路由键发送持久化消息")
    @PostMapping("/send/persistent")
    public ResponseEntity<Map<String, Object>> sendPersistentMessage(
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "持久化消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
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
    @ApiOperation(value = "发送对象消息", notes = "向指定交换机和路由键发送对象消息")
    @PostMapping("/send/object")
    public ResponseEntity<Map<String, Object>> sendObjectMessage(
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestBody LikeMessage likeMessage) {

        Map<String, Object> response = new HashMap<>();
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, likeMessage);
            response.put("code", 200);
            response.put("message", "对象消息发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "对象消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
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
    @ApiOperation(value = "发送带回调的消息", notes = "向指定交换机和路由键发送带确认回调的消息")
    @PostMapping("/send/with-callback")
    public ResponseEntity<Map<String, Object>> sendMessageWithCallback(
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "带回调消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
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
    @ApiOperation(value = "发送批量消息", notes = "向指定交换机和路由键发送批量消息")
    @PostMapping("/send/batch")
    public ResponseEntity<Map<String, Object>> sendBatchMessages(
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "批量消息发送失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取RabbitMQ连接状态
     * 
     * @return 响应结果
     */
    @ApiOperation(value = "获取RabbitMQ连接状态", notes = "检查RabbitMQ连接是否正常")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getRabbitMQStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查连接是否正常
            rabbitTemplate.getConnectionFactory().createConnection().close();
            response.put("code", 200);
            response.put("message", "RabbitMQ连接正常");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "RabbitMQ连接异常: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}