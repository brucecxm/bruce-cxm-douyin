package com.bruce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 消息队列消费点赞数更新
    public void updateLikeCount(int videoid) {
        System.out.println("Message sent to queue to update like count for content: "  );

    }

    // 模拟发送消息到消息队列（例如 RabbitMQ）
    public void sendMessage(Long contentId,String type) {
        // 这里省略了消息队列的具体实现
        // 可以将 contentId 推送到队列，后台系统会处理并更新 MySQL 中的数据
        System.out.println("Message sent to queue to update like count for content: " + contentId);
    }
}
