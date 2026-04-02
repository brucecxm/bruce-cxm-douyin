package com.bruce.listener;

import com.bruce.dto.LikeMsg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoLikeConsumer {
    @Autowired
    private AsyncTaskService asyncTaskService;

    // 队列1
    @RabbitListener(
        queues = "video.like.queue.1",
        concurrency = "5-10"  // 最小5，最大10，所有版本都支持
    )
    public void consumeQueue1(LikeMsg message) {
        System.out.println("【队列1】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("1");
    }

    // 队列2
    @RabbitListener(
        queues = "video.like.queue.2",
        concurrency = "5-10"
    )
    public void consumeQueue2(LikeMsg message) {
        System.out.println("【队列2】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("2");
    }

    // 队列3
    @RabbitListener(
        queues = "video.like.queue.3",
        concurrency = "5-10"
    )
    public void consumeQueue3(LikeMsg message) {
        System.out.println("【队列3】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("3");
    }

    // 队列4
    @RabbitListener(
        queues = "video.like.queue.4",
        concurrency = "5-10"
    )
    public void consumeQueue4(LikeMsg message) {
        System.out.println("【队列4】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("4");
    }
}
