package com.bruce.listener;

import com.bruce.dto.LikeMsg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class VideoLikeConsumer {
    // 使用刚才定义的线程池
    @Async("likeThreadPool")
    public void doAsyncTask() {
        System.out.println("异步任务开始执行，线程名：" + Thread.currentThread().getName());
    }

    // 监听队列1
    @RabbitListener(queues = "video.like.queue.1")
    public void consumeQueue1(LikeMsg message) {
        System.out.println("【队列1】处理点赞消息：" + message);

    }

    // 监听队列2
    @RabbitListener(queues = "video.like.queue.2")
    public void consumeQueue2(LikeMsg message) {
        System.out.println("【队列2】处理点赞消息：" + message);
    }

    // 监听队列3
    @RabbitListener(queues = "video.like.queue.3")
    public void consumeQueue3(LikeMsg message) {
        System.out.println("【队列3】处理点赞消息：" + message);
    }

    // 监听队列4
    @RabbitListener(queues = "video.like.queue.4")
    public void consumeQueue4(LikeMsg message) {
        System.out.println("【队列4】处理点赞消息：" + message);
    }
}
