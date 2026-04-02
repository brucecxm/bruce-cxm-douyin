package com.bruce.listener;

import com.bruce.dto.LikeMsg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class VideoLikeConsumer {
    @Autowired
    private AsyncTaskService asyncTaskService;

    // 监听队列1
    @RabbitListener(queues = "video.like.queue.1")
    public void consumeQueue1(LikeMsg message) {
        System.out.println("【队列1】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("1");
    }

    // 监听队列2
    @RabbitListener(queues = "video.like.queue.2")
    public void consumeQueue2(LikeMsg message) {
        System.out.println("【队列2】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("2");

    }

    // 监听队列3
    @RabbitListener(queues = "video.like.queue.3")
    public void consumeQueue3(LikeMsg message) {
        System.out.println("【队列3】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("3");

    }

    // 监听队列4
    @RabbitListener(queues = "video.like.queue.4")
    public void consumeQueue4(LikeMsg message) {
        System.out.println("【队列4】处理点赞消息：" + message);
        asyncTaskService.doAsyncTask("4");

    }
}
