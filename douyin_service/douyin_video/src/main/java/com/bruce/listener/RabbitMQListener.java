package com.bruce.listener;

import com.bruce.dto.LikeMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RabbitMQListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 监听简单文本消息
     * 
     * @param message 消息内容
     * @param channel 通道信息
     */
    @RabbitListener(queues = "${rabbitmq.queue.simple:default-simple-queue}", concurrency = "5-10")
    @RabbitHandler
    public void handleSimpleMessage(String message, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到简单消息: " + message);
            System.out.println("消息ID: " + amqpMessage.getMessageProperties().getMessageId());
            System.out.println("交换机: " + amqpMessage.getMessageProperties().getReceivedExchange());
            System.out.println("路由键: " + amqpMessage.getMessageProperties().getReceivedRoutingKey());

            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理简单消息失败: " + e.getMessage());
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                System.err.println("拒绝消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 监听对象消息
     * 
     * @param likeMessage 点赞消息对象
     * @param channel     通道信息
     */
    @RabbitListener(queues = "${rabbitmq.queue.object:default-object-queue}", concurrency = "5-10")
    @RabbitHandler
    public void handleObjectMessage(LikeMessage likeMessage, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到对象消息: ");
            System.out.println("消息ID: " + amqpMessage.getMessageProperties().getMessageId());
            System.out.println("视频ID: " + likeMessage.getVideoId());
            System.out.println("用户ID: " + likeMessage.getUserId());
            System.out.println("操作类型: " + likeMessage.getType());
            System.out.println("时间戳: " + likeMessage.getTimestamp());

            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理对象消息失败: " + e.getMessage());
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                System.err.println("拒绝消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 监听批量消息
     * 
     * @param messages 消息列表
     * @param channel  通道信息
     */
    @RabbitListener(queues = "${rabbitmq.queue.batch:default-batch-queue}", concurrency = "5-10")
    @RabbitHandler
    public void handleBatchMessages(List<LikeMessage> messages, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到批量消息，共 " + messages.size() + " 条");
            for (LikeMessage message : messages) {
                System.out.println("  - 视频ID: " + message.getVideoId() + ", 用户ID: " + message.getUserId() + ", 操作: "
                        + message.getType());
            }

            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理批量消息失败: " + e.getMessage());
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                System.err.println("拒绝消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 监听Map类型消息
     * 
     * @param mapMessage Map类型消息
     * @param channel    通道信息
     */
    @RabbitListener(queues = "${rabbitmq.queue.map:default-map-queue}", concurrency = "5-10")
    @RabbitHandler
    public void handleMapMessage(Map<String, Object> mapMessage, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到Map类型消息: ");
            for (Map.Entry<String, Object> entry : mapMessage.entrySet()) {
                System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
            }

            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理Map类型消息失败: " + e.getMessage());
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                System.err.println("拒绝消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 监听死信队列消息
     * 
     * @param message 消息内容
     * @param channel 通道信息
     */
    @RabbitListener(queues = "${rabbitmq.queue.dead-letter:default-dead-letter-queue}", concurrency = "2-5")
    @RabbitHandler
    public void handleDeadLetterMessage(Object message, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到死信队列消息: " + message);
            System.out.println("原消息ID: " + amqpMessage.getMessageProperties().getMessageId());
            System.out.println("原交换机: " + amqpMessage.getMessageProperties().getReceivedExchange());
            System.out.println("原路由键: " + amqpMessage.getMessageProperties().getReceivedRoutingKey());
            System.out.println("死信原因: " + amqpMessage.getMessageProperties().getHeaders().get("x-death"));

            // 死信消息直接确认，不再重新入队
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理死信队列消息失败: " + e.getMessage());
            try {
                // 死信消息处理失败，直接确认，避免死信队列阻塞
                channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception ex) {
                System.err.println("确认死信消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 监听点赞相关消息 - 使用通用消息处理
     */
    @RabbitListener(queues = "like.video.queue", concurrency = "10-20")
    public void handleLikeMessage(Message amqpMessage) {
        try {
            // 获取消息内容和类型
            byte[] body = amqpMessage.getBody();
            String contentType = amqpMessage.getMessageProperties().getContentType();
            String messageType = amqpMessage.getMessageProperties().getHeaders().getOrDefault("__TypeId__", "unknown")
                    .toString();

            System.out.println("接收到点赞队列消息: ");
            System.out.println("  消息类型: " + messageType);
            System.out.println("  内容类型: " + contentType);
            System.out.println("  消息ID: " + amqpMessage.getMessageProperties().getMessageId());
            System.out.println("  交换机: " + amqpMessage.getMessageProperties().getReceivedExchange());
            System.out.println("  路由键: " + amqpMessage.getMessageProperties().getReceivedRoutingKey());

            // 根据消息类型进行不同处理
            if (messageType.contains("LikeMessage")) {
                // 处理LikeMessage对象
                Object convertedMessage = rabbitTemplate.getMessageConverter().fromMessage(amqpMessage);
                if (convertedMessage instanceof LikeMessage) {
                    LikeMessage likeMessage = (LikeMessage) convertedMessage;
                    System.out.println("  视频ID: " + likeMessage.getVideoId());
                    System.out.println("  用户ID: " + likeMessage.getUserId());
                    System.out.println("  操作类型: " + likeMessage.getType());
                    System.out.println("  时间戳: " + likeMessage.getTimestamp());

                    // 这里可以添加具体的点赞业务逻辑
                    // 例如更新数据库中的点赞数、记录点赞日志等
                }
            } else if (messageType.contains("String")) {
                // 处理String消息
                Object convertedMessage = rabbitTemplate.getMessageConverter().fromMessage(amqpMessage);
                if (convertedMessage instanceof String) {
                    String stringMessage = (String) convertedMessage;
                    System.out.println("  消息内容: " + stringMessage);
                }
            } else {
                // 处理其他类型消息
                Object objectMessage = rabbitTemplate.getMessageConverter().fromMessage(amqpMessage);
                System.out.println("  消息内容: " + objectMessage);
                System.out.println("  消息类型: " + (objectMessage != null ? objectMessage.getClass().getName() : "null"));
            }

            // 消息处理成功，Spring会自动确认
        } catch (Exception e) {
            System.err.println("处理点赞消息失败: " + e.getMessage());
            e.printStackTrace();
            // 消息处理失败，Spring会自动拒绝并重新入队（根据配置）
        }
    }

    /**
     * 监听批量点赞消息
     * 
     * @param likeMessages 点赞消息列表
     * @param channel      通道信息
     */
    @RabbitListener(queues = "like.video.batch.queue", concurrency = "10-20")
    @RabbitHandler
    public void handleBatchLikeMessages(List<LikeMessage> likeMessages, Channel channel, Message amqpMessage) {
        try {
            System.out.println("接收到批量点赞消息，共 " + likeMessages.size() + " 条");

            // 这里可以添加批量处理点赞业务逻辑
            // 例如批量更新数据库中的点赞数、批量记录点赞日志等
            for (LikeMessage message : likeMessages) {
                System.out.println("  - 视频ID: " + message.getVideoId() + ", 用户ID: " + message.getUserId() + ", 操作: "
                        + message.getType());
            }

            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("处理批量点赞消息失败: " + e.getMessage());
            e.printStackTrace();
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                System.err.println("拒绝批量点赞消息失败: " + ex.getMessage());
            }
        }
    }
}