package com.bruce.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean("likeThreadPool") // 给线程池起名字：点赞专用线程池
    public Executor likeThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心参数（生产级）
        executor.setCorePoolSize(5);      // 核心线程
        executor.setMaxPoolSize(20);      // 最大线程
        executor.setQueueCapacity(100);    // 队列大小
        executor.setKeepAliveSeconds(60); // 空闲时间

        // 拒绝策略：队列满了丢弃，不影响主接口
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        executor.initialize();
        return executor;
    }



    @Bean("videoDownLoThreadPool") // 给线程池起名字：点赞专用线程池
    public Executor videoDownLoThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心参数（生产级）
        executor.setCorePoolSize(5);      // 核心线程
        executor.setMaxPoolSize(20);      // 最大线程
        executor.setQueueCapacity(100);    // 队列大小
        executor.setKeepAliveSeconds(60); // 空闲时间

        // 拒绝策略：队列满了丢弃，不影响主接口
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        executor.initialize();
        return executor;
    }




}
