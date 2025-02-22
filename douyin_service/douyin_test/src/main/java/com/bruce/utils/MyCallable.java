package com.bruce.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//一般简单的就用这个thread类  但是java只能单继承 如果类已经继承了一个类 不能使用此方法
//runnable解决了java只能单继承的缺点  但是没有返回值 无法获取返回值
//callable解决了runable无法获取返回值的缺点

/*
* Callable 通常与线程池结合使用，尤其是在需要执行多个并发任务时，
* 线程池能够高效地管理线程资源，并且通过线程池的 ExecutorService 来提交 Callable 任务。
* */
public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        // 执行任务并返回一个结果
        System.out.println("Callable thread is running");
        return 42; // 返回的结果
    }

    public static void main(String[] args) {
        // 创建一个线程池，最大线程数为 1
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 创建 Callable 任务
        MyCallable myCallable = new MyCallable();

        // 提交任务并返回 Future 对象
        Future<Integer> future = executorService.submit(myCallable);

        try {
            // 获取任务执行的返回值，阻塞直到任务完成
            Integer result = future.get();
            System.out.println("Task result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // 关闭线程池
        }
    }
}
