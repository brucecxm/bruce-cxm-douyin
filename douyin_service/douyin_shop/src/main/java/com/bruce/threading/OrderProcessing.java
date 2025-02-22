package com.bruce.threading;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/*
* 在电商系统中，订单处理是一个典型的并发场景。每当用户下单时，系统需要执行一系列操作，如库存检查、价格计算、支付处理、优惠券验证、订单保存等。
* 如果这些操作是顺序执行的，可能会导致响应时间过长，影响用户体验。因此，多线程可以用来并发处理这些操作，提高处理效率。
*
* */
@Service
public class OrderProcessing {

    // 模拟的操作方法
    public void checkStock() {
        // 假设这是库存检查操作
        System.out.println("Checking stock...");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Stock check completed.");
    }

    public void calculatePrice() {
        // 假设这是价格计算操作
        System.out.println("Calculating price...");
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Price calculated.");
    }

    public void processPayment() {
        // 假设这是支付处理操作
        System.out.println("Processing payment...");
        try { Thread.sleep(1200); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Payment processed.");
    }

    public void verifyCoupon() {
        // 假设这是优惠券验证操作
        System.out.println("Verifying coupon...");
        try { Thread.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Coupon verified.");
    }

    public void saveOrder() {
        // 假设这是订单保存操作
        System.out.println("Saving order...");
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Order saved.");
    }

    // 使用线程池进行异步处理
    public void processOrder() {
        ExecutorService executor = Executors.newFixedThreadPool(5);  // 创建线程池，最多允许5个线程

        // 提交各个任务到线程池异步处理
        Future<?> stockFuture = executor.submit(() -> checkStock());
        Future<?> priceFuture = executor.submit(() -> calculatePrice());
        Future<?> paymentFuture = executor.submit(() -> processPayment());
        Future<?> couponFuture = executor.submit(() -> verifyCoupon());
        Future<?> saveFuture = executor.submit(() -> saveOrder());

        try {
            // 等待所有任务完成
            stockFuture.get();
            priceFuture.get();
            paymentFuture.get();
            couponFuture.get();
            saveFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();  // 关闭线程池
        }

        System.out.println("Order processing completed.");
    }


}
