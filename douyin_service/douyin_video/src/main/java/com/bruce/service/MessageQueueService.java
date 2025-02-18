package com.bruce.service;

import com.bruce.dao.VideoOneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private VideoOneDao VideoOneDao;



    // 消息队列消费点赞数更新
    public void updateLikeCount(int videoid) {
        String redisKey = "like:content:" + videoid;

        // 4. 获取 Redis 中的点赞用户总数
        long likeCount = redisTemplate.opsForSet().size(redisKey);
int one=(int)likeCount;
        // 5. 更新 MySQL 数据库中的点赞数
        VideoOneDao.getlikeexitMap(videoid, one);
    }

    // 模拟发送消息到消息队列（例如 RabbitMQ）
    public void sendMessage(int contentId) {
        // 这里省略了消息队列的具体实现
        // 可以将 contentId 推送到队列，后台系统会处理并更新 MySQL 中的数据
        System.out.println("Message sent to queue to update like count for content: " + contentId);
    }
}
