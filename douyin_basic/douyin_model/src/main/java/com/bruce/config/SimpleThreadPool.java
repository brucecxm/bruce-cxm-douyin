package com.bruce.config;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 简易线程池实现（基于工作线程模式）
 */
public class SimpleThreadPool {
    // 任务队列：存储待执行的Runnable任务（阻塞队列，线程安全）
    private final BlockingQueue<Runnable> taskQueue;
    // 工作线程数组：负责从队列中取任务并执行
    private final Worker[] workers;

    /**
     * 初始化线程池
     * @param corePoolSize 核心线程数（工作线程的数量）
     * @param queueCapacity 任务队列的容量
     */
    public SimpleThreadPool(int corePoolSize, int queueCapacity) {
        // 初始化任务队列（使用ArrayBlockingQueue，有界队列，避免内存溢出）
        this.taskQueue = new ArrayBlockingQueue<>(queueCapacity);
        // 初始化工作线程数组
        this.workers = new Worker[corePoolSize];

        // 创建并启动所有工作线程
        for (int i = 0; i < corePoolSize; i++) {
            workers[i] = new Worker();
            workers[i].start(); // 启动工作线程
        }
    }

    /**
     * 向线程池提交任务
     * @param task 待执行的任务（Runnable）
     * @throws InterruptedException 当线程被中断时抛出
     */
    public void submit(Runnable task) throws InterruptedException {
        taskQueue.put(task); // 将任务放入队列（若队列满则阻塞等待）
    }

    /**
     * 关闭线程池（优雅关闭：执行完剩余任务后退出）
     */
    public void shutdown() {
        // 停止所有工作线程
        for (Worker worker : workers) {
            worker.stop();
        }
        // 等待所有工作线程执行完毕
        for (Worker worker : workers) {
            try {
                worker.thread.join(); // 等待线程终止
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        }
        System.out.println("线程池已关闭");
    }

    /**
     * 工作线程类：负责从任务队列中获取并执行任务
     */
    private class Worker implements Runnable {
        // 工作线程的实际线程对象
        final Thread thread;
        // 线程运行标记（volatile保证多线程可见性）
        volatile boolean running = true;

        public Worker() {
            // 将当前Worker实例作为线程的执行体
            this.thread = new Thread(this, "Worker-" + Thread.currentThread().getId());
        }

        // 启动工作线程
        public void start() {
            thread.start();
        }

        // 停止工作线程（设置标记，让循环退出）
        public void stop() {
            running = false;
            // 中断阻塞中的线程（若线程在take()处阻塞，需唤醒它）
            thread.interrupt();
        }

        @Override
        public void run() {
            // 循环条件：线程运行中 或者 队列中还有剩余任务
            while (running || !taskQueue.isEmpty()) {
                try {
                    // 从队列中取任务（核心逻辑）：
                    // 1. 若队列有任务，立即返回队首任务
                    // 2. 若队列为空，阻塞当前线程，直到有任务或被中断
                    Runnable task = taskQueue.take();
                    // 执行任务（在当前工作线程中运行）
                    System.out.println("线程 " + thread.getName() + " 执行任务");
                    task.run();
                } catch (InterruptedException e) {
                    // 若线程被中断（如调用stop()时），退出循环
                    if (!running) {
                        break;
                    }
                }
            }
            System.out.println("线程 " + thread.getName() + " 已退出");
        }
    }

    // 测试代码
    public static void main(String[] args) throws InterruptedException {
        // 创建线程池：2个工作线程，队列容量为5
        SimpleThreadPool threadPool = new SimpleThreadPool(2, 5);

        // 提交10个任务到线程池
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.submit(() -> {
                try {
                    // 模拟任务执行耗时（1秒）
                    Thread.sleep(1000);
                    System.out.println("任务 " + taskId + " 执行完毕");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            System.out.println("提交任务 " + taskId);
        }

        // 等待所有任务执行完毕（实际中需更严谨的判断，此处简化）
        Thread.sleep(10000);
        // 关闭线程池
        threadPool.shutdown();
    }
}
