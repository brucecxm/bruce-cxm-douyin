package com.bruce.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 手动实现线程复用的简单示例（不推荐）
class ReusableThread extends Thread {
    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public ReusableThread() {
        start(); // 启动线程，进入循环等待任务
    }

    // 提交任务
    public void submit(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    // 停止线程
    public void stopThread() {
        running = false;
        interrupt(); // 唤醒阻塞在take()的线程
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 从队列取任务，没有任务则阻塞
                Runnable task = taskQueue.take();
                task.run(); // 执行任务
            } catch (InterruptedException e) {
                // 处理中断（如线程停止）
                if (!running) break;
            }
        }
    }
}

// 使用
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ReusableThread thread = new ReusableThread();

        thread.submit(() -> System.out.println("任务1")); // 复用线程执行任务1
        thread.submit(() -> System.out.println("任务2")); // 复用线程执行任务2
        thread.stopThread();
    }
}
