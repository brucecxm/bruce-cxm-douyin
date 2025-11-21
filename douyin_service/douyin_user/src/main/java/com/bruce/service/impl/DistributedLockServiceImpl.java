package com.bruce.service.impl;

import com.bruce.entitys.LockType;
import com.bruce.service.DistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分布式锁服务实现类
 * 提供不同类型锁的实现
 */
@Service
@Slf4j
public class DistributedLockServiceImpl implements DistributedLockService {
    
    // 内存锁存储
    private final Map<String, Lock> memoryLocks = new ConcurrentHashMap<>();
    
    @Override
    public boolean acquireLock(String lockKey, LockType lockType) {
        return acquireLock(lockKey, lockType, 5000); // 默认超时5秒
    }
    
    @Override
    public boolean acquireLock(String lockKey, LockType lockType, long timeout) {
        try {
            switch (lockType) {
                case REDIS_LOCK:
                    return acquireRedisLock(lockKey, timeout);
                case ZOOKEEPER_LOCK:
                    return acquireZookeeperLock(lockKey, timeout);
                case DATABASE_LOCK:
                    return acquireDatabaseLock(lockKey, timeout);
                case IN_MEMORY_LOCK:
                    return acquireMemoryLock(lockKey, timeout);
                default:
                    // 默认使用Redis锁
                    return acquireRedisLock(lockKey, timeout);
            }
        } catch (Exception e) {
            log.error("获取锁失败: lockKey={}, lockType={}, error={}", lockKey, lockType, e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean releaseLock(String lockKey, LockType lockType) {
        try {
            switch (lockType) {
                case REDIS_LOCK:
                    return releaseRedisLock(lockKey);
                case ZOOKEEPER_LOCK:
                    return releaseZookeeperLock(lockKey);
                case DATABASE_LOCK:
                    return releaseDatabaseLock(lockKey);
                case IN_MEMORY_LOCK:
                    return releaseMemoryLock(lockKey);
                default:
                    // 默认释放Redis锁
                    return releaseRedisLock(lockKey);
            }
        } catch (Exception e) {
            log.error("释放锁失败: lockKey={}, lockType={}, error={}", lockKey, lockType, e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取Redis分布式锁
     */
    private boolean acquireRedisLock(String lockKey, long timeout) {
        // 这里是Redis锁的模拟实现
        // 实际项目中应该使用RedisTemplate或Redisson实现
        log.info("获取Redis锁: {}", lockKey);
        // 模拟锁获取成功
        return true;
    }
    
    /**
     * 释放Redis分布式锁
     */
    private boolean releaseRedisLock(String lockKey) {
        log.info("释放Redis锁: {}", lockKey);
        // 模拟锁释放成功
        return true;
    }
    
    /**
     * 获取Zookeeper分布式锁
     */
    private boolean acquireZookeeperLock(String lockKey, long timeout) {
        // 这里是Zookeeper锁的模拟实现
        // 实际项目中应该使用Curator框架实现
        log.info("获取Zookeeper锁: {}", lockKey);
        // 模拟锁获取成功
        return true;
    }
    
    /**
     * 释放Zookeeper分布式锁
     */
    private boolean releaseZookeeperLock(String lockKey) {
        log.info("释放Zookeeper锁: {}", lockKey);
        // 模拟锁释放成功
        return true;
    }
    
    /**
     * 获取数据库悲观锁
     */
    private boolean acquireDatabaseLock(String lockKey, long timeout) {
        // 数据库锁通常通过SQL的SELECT ... FOR UPDATE实现
        // 这里是模拟实现
        log.info("获取数据库锁: {}", lockKey);
        // 模拟锁获取成功
        return true;
    }
    
    /**
     * 释放数据库悲观锁
     */
    private boolean releaseDatabaseLock(String lockKey) {
        log.info("释放数据库锁: {}", lockKey);
        // 数据库锁通常通过事务提交自动释放
        // 模拟锁释放成功
        return true;
    }
    
    /**
     * 获取内存锁
     */
    private boolean acquireMemoryLock(String lockKey, long timeout) {
        // 使用ReentrantLock作为内存锁
        Lock lock = memoryLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());
        log.info("获取内存锁: {}", lockKey);
        try {
            return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    /**
     * 释放内存锁
     */
    private boolean releaseMemoryLock(String lockKey) {
        Lock lock = memoryLocks.get(lockKey);
        if (lock != null) {
            log.info("释放内存锁: {}", lockKey);
            lock.unlock();
            // 清理不再使用的锁
            memoryLocks.remove(lockKey);
            return true;
        }
        return false;
    }
}