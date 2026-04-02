package com.bruce.listener;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

@Service
public class AsyncTaskService {

    // 指定使用你自定义的线程池 likeThreadPool
    @Async("likeThreadPool")
    public void doAsyncTask(String msg) {
        try {
            // 异步任务 + 打印线程名
            System.out.println("异步任务开始执行：" + msg);
            System.out.println("当前异步线程名称：" + Thread.currentThread().getName());

            // 【核心】延时 4 秒（4000 毫秒）
            Thread.sleep(4000);

            System.out.println("异步任务执行完成：" + msg);
        } catch (InterruptedException e) {
            // 线程中断异常处理
            Thread.currentThread().interrupt();
            System.out.println("异步任务被中断：" + msg);
        }
    }
}
