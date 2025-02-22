package com.bruce.threading;
import java.util.concurrent.*;


/*
* 电商支付过程中需要确保每一笔交易都能顺利处理，避免并发导致的数据冲突或者重复支付等问题。常见的多线程应用场景有：

支付回调处理：支付完成后，支付网关会回调电商系统，通知支付结果。为了防止因并发请求导致多次处理相同的支付结果，
* 可以使用多线程并发处理支付回调，并通过去重机制确保每笔支付只被处理一次。
支付状态更新：多个线程可以并行更新用户的支付状态（如支付成功、支付失败、退款等）。  用代码举例子
* */


/*
* 2. 支付状态更新（多线程并发处理）
支付状态更新是指多个线程并发更新用户的支付状态（如支付成功、支付失败、退款等）。
* 为了确保支付状态更新的准确性和一致性，我们可以使用 线程池 来并行处理多个支付状态更新操作
* */

/*
*
* 代码解析：
线程池管理：

使用 ExecutorService（线程池）来管理并发任务，避免频繁创建和销毁线程的开销。
newFixedThreadPool(10) 创建一个固定大小的线程池，最多同时处理 10 个支付状态更新任务。
并发执行：

每个支付状态更新任务都通过 submit 方法提交到线程池，进行并行处理。每个任务会更新一个支付 ID 的状态。
支付状态更新：

假设支付状态更新操作涉及数据库操作（如更新订单表中的支付状态），在实际场景中需要确保数据库操作的原子性和一致性（可以考虑事务、分布式锁等机制）。
线程池关闭：

在任务完成后，关闭线程池来释放资源。
* */


public class PaymentStatusUpdater {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    // 模拟支付状态更新
    public void updatePaymentStatus(String paymentId, String newStatus) {
        System.out.println("支付ID: " + paymentId + ", 更新支付状态为: " + newStatus);
        // 这里可以是调用数据库更新支付状态的逻辑
    }

    // 模拟多线程并发处理支付状态更新
    public void processPaymentStatusUpdates() {
        // 模拟多个支付状态更新任务
        for (int i = 0; i < 10; i++) {
            final String paymentId = "payment_" + i;
            final String newStatus = i % 2 == 0 ? "SUCCESS" : "FAILED";  // 模拟成功和失败的支付状态
            executorService.submit(() -> updatePaymentStatus(paymentId, newStatus));
        }
    }

    public static void main(String[] args) {
        PaymentStatusUpdater updater = new PaymentStatusUpdater();
        updater.processPaymentStatusUpdates();

        // 关闭线程池
        executorService.shutdown();
    }
}
