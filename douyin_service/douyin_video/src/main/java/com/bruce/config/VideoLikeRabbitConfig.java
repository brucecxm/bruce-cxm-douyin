package com.bruce.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频点赞 一致性哈希交换机配置
 * 相同 video_id 永久路由到同一个队列，保证顺序 & 避免锁竞争
 */
@Configuration
public class VideoLikeRabbitConfig {

    /** 一致性哈希交换机 */
    public static final String VIDEO_LIKE_EXCHANGE = "video.like.exchange";

    /** 队列前缀 */
    public static final String QUEUE_PREFIX = "video.like.queue.";

    /** 队列数量 */
    private static final int QUEUE_COUNT = 4;

    // ====================== 1. 声明一致性哈希交换机（正常） ======================
    @Bean
    public CustomExchange videoLikeExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("hash-key", "routing-key");

        return new CustomExchange(
                VIDEO_LIKE_EXCHANGE,
                "x-consistent-hash",
                true,
                false,
                args
        );
    }

    // ====================== 2. 修复：逐个声明队列（必须单个@Bean） ======================
    @Bean
    public Queue videoLikeQueue1() {
        return new Queue(QUEUE_PREFIX + 1, true);
    }

    @Bean
    public Queue videoLikeQueue2() {
        return new Queue(QUEUE_PREFIX + 2, true);
    }

    @Bean
    public Queue videoLikeQueue3() {
        return new Queue(QUEUE_PREFIX + 3, true);
    }

    @Bean
    public Queue videoLikeQueue4() {
        return new Queue(QUEUE_PREFIX + 4, true);
    }

    // ====================== 3. 修复：逐个绑定（必须单个@Bean） ======================
    @Bean
    public Binding bindingVideoLikeQueue1(CustomExchange videoLikeExchange, Queue videoLikeQueue1) {
        return BindingBuilder.bind(videoLikeQueue1)
                .to(videoLikeExchange)
                .with("1")
                .noargs();
    }

    @Bean
    public Binding bindingVideoLikeQueue2(CustomExchange videoLikeExchange, Queue videoLikeQueue2) {
        return BindingBuilder.bind(videoLikeQueue2)
                .to(videoLikeExchange)
                .with("1")
                .noargs();
    }

    @Bean
    public Binding bindingVideoLikeQueue3(CustomExchange videoLikeExchange, Queue videoLikeQueue3) {
        return BindingBuilder.bind(videoLikeQueue3)
                .to(videoLikeExchange)
                .with("1")
                .noargs();
    }

    @Bean
    public Binding bindingVideoLikeQueue4(CustomExchange videoLikeExchange, Queue videoLikeQueue4) {
        return BindingBuilder.bind(videoLikeQueue4)
                .to(videoLikeExchange)
                .with("1")
                .noargs();
    }
}
