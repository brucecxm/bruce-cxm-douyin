package com.bruce.threading;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
* 在电商业务中，库存是一个需要频繁更新的资源。尤其是在秒杀、抢购等场景下，
* 商品库存会因为大量用户并发操作而发生变化。为了确保库存更新的准确性和防止超卖，多线程和并发控制就显得尤为重要。
* */


/*
* 在电商业务中，尤其是秒杀或抢购等高并发场景下，确保库存的准确性至关重要。我们可以使用 乐观锁 和 分布式锁 来控制库存的并发更新。
* */

/*
* 分布式锁主要用于高并发分布式系统中，确保在多台服务器上进行库存更新时，
* 每次只有一个线程可以操作库存，避免并发冲突。我们可以使用 Redis 来实现分布式锁，确保只有一个线程能够执行库存更新操作。


 * */
public class DistributedLockExample {

    private static RedissonClient redisson;

    // 初始化 Redisson 客户端
    public static void initRedisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        redisson = Redisson.create(config);
    }

    // 使用分布式锁来更新库存
    public void updateStockWithDistributedLock(int productId, int quantity) {
        RLock lock = redisson.getLock("stock-lock-" + productId);

        try {
            // 尝试获取分布式锁，最多等待 10 秒，锁定时间为 5 秒
            if (lock.tryLock(10, 5, java.util.concurrent.TimeUnit.SECONDS)) {
                System.out.println("锁定库存，开始更新库存...");

                // 这里模拟库存更新过程
                // 假设库存为 100，实际项目中要根据商品 ID 查询数据库中的库存
                int stock = 100;

                // 如果库存足够，扣除库存
                if (stock >= quantity) {
                    stock -= quantity;
                    System.out.println("库存更新成功，扣除数量：" + quantity);
                } else {
                    System.out.println("库存不足，无法更新");
                }
            } else {
                System.out.println("获取分布式锁失败，稍后重试");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        initRedisson();  // 初始化 Redis 客户端

        DistributedLockExample example = new DistributedLockExample();

        // 模拟多个线程并发扣减库存
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            final int quantity = 1;  // 每次扣减 1 个库存
            executor.submit(() -> example.updateStockWithDistributedLock(1, quantity));
        }

        executor.shutdown();
    }
}
