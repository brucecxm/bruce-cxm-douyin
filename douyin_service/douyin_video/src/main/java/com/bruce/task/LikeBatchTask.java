package com.bruce.task;

import com.bruce.dto.LikeMessage;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class LikeBatchTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定时批量发送点赞消息到MQ
     * 每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void batchSendLikeMessages() {
        // 获取所有批量点赞key
        Set<String> batchKeys = redisTemplate.keys("like:batch:*");
        if (batchKeys == null || batchKeys.isEmpty()) {
            return;
        }

        for (String batchKey : batchKeys) {
            // 获取当前key下的所有消息
            List<LikeMessage> messages = redisTemplate.opsForList().range(batchKey, 0, -1);
            if (messages != null && !messages.isEmpty()) {
                // 批量发送消息到MQ，并设置持久化
                rabbitTemplate.convertAndSend("like.exchange", "like.video.batch", messages, message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });
                // 清空Redis列表
                redisTemplate.delete(batchKey);
            }
        }
    }
}