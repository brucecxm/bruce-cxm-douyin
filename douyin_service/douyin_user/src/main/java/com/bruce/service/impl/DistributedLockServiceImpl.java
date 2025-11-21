package com.bruce.service.impl;

import com.bruce.entitys.LockType;
import com.bruce.service.DistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
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
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    // 内存锁存储
    private final Map<String, Lock> memoryLocks = new ConcurrentHashMap<>();
    
    // 存储每个锁的唯一标识，用于安全释放锁
    private final Map<String, String> lockIdentifiers = new ConcurrentHashMap<>();
    
    // Lua脚本用于原子性释放锁
    private static final String RELEASE_LOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "    return redis.call('del', KEYS[1]) " +
        "else " +
        "    return 0 " +
        "end";

    
    private final DefaultRedisScript<Long> releaseLockScript = new DefaultRedisScript<>(RELEASE_LOCK_SCRIPT, Long.class);
    
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
                    log.warn("Zookeeper锁暂未实现真正的分布式锁功能，使用Redis锁替代");
                    return acquireRedisLock(lockKey, timeout);
                case DATABASE_LOCK:
                    log.warn("数据库锁暂未实现真正的分布式锁功能，使用Redis锁替代");
                    return acquireRedisLock(lockKey, timeout);
                case IN_MEMORY_LOCK:
                    return acquireMemoryLock(lockKey, timeout);
                default:
                    // 默认使用Redis锁
                    return acquireRedisLock(lockKey, timeout);
            }
        } catch (Exception e) {
            log.error("获取锁失败: lockKey={}, lockType={}, error={}", lockKey, lockType, e.getMessage(), e);
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
                case DATABASE_LOCK:
                    // 对于未实现的锁类型，也尝试释放Redis锁
                    return releaseRedisLock(lockKey);
                case IN_MEMORY_LOCK:
                    return releaseMemoryLock(lockKey);
                default:
                    // 默认释放Redis锁
                    return releaseRedisLock(lockKey);
            }
        } catch (Exception e) {
            log.error("释放锁失败: lockKey={}, lockType={}, error={}", lockKey, lockType, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取Redis分布式锁
     * 使用SETNX命令和超时时间实现
     */
    private boolean acquireRedisLock(String lockKey, long timeout) {
        String lockId = UUID.randomUUID().toString();
        log.info("尝试获取Redis锁: {}, 超时时间: {}ms", lockKey, timeout);
        
        try {
            // 使用SET命令的NX和EX选项，原子性地设置锁和超时时间
            Boolean success = redisTemplate.opsForValue().setIfAbsent(
                lockKey, 
                lockId, 
                timeout, 
                TimeUnit.MILLISECONDS
            );
            
            if (Boolean.TRUE.equals(success)) {
                // 存储锁标识，用于后续安全释放
                lockIdentifiers.put(lockKey, lockId);
                log.info("成功获取Redis锁: {}", lockKey);
                return true;
            } else {
                log.warn("获取Redis锁失败，锁已被占用: {}", lockKey);
                return false;
            }
        } catch (Exception e) {
            log.error("获取Redis锁时发生异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 释放Redis分布式锁
     * 使用Lua脚本确保原子性操作，防止释放其他线程的锁
     */
    private boolean releaseRedisLock(String lockKey) {
        String lockId = lockIdentifiers.get(lockKey);
        if (lockId == null) {
            log.warn("尝试释放未获取的锁: {}", lockKey);
            return false;
        }
        
        log.info("尝试释放Redis锁: {}", lockKey);
        
        try {
            // 使用Lua脚本原子性地验证并释放锁
            Long result = redisTemplate.execute(
                releaseLockScript,
                Collections.singletonList(lockKey),
                lockId
            );
            
            // 清理本地存储的锁标识
            lockIdentifiers.remove(lockKey);
            
            boolean success = result != null && result > 0;
            if (success) {
                log.info("成功释放Redis锁: {}", lockKey);
            } else {
                log.warn("释放Redis锁失败，锁可能已过期或被其他线程占用: {}", lockKey);
            }
            return success;
        } catch (Exception e) {
            log.error("释放Redis锁时发生异常: {}", e.getMessage(), e);
            // 即使释放失败，也清理本地状态
            lockIdentifiers.remove(lockKey);
            return false;
        }
    }
    
    /**
     * 获取Zookeeper分布式锁
     * 注意：此实现仍是模拟的，实际项目中应使用Curator框架
     */
    private boolean acquireZookeeperLock(String lockKey, long timeout) {
        // 模拟实现，实际应使用Curator框架
        log.warn("Zookeeper锁模拟实现: {}", lockKey);
        return false;
    }
    
    /**
     * 释放Zookeeper分布式锁
     */
    private boolean releaseZookeeperLock(String lockKey) {
        log.warn("Zookeeper锁模拟释放: {}", lockKey);
        return false;
    }
    
    /**
     * 获取数据库悲观锁
     * 注意：此实现仍是模拟的，实际应通过事务和SELECT FOR UPDATE实现
     */
    private boolean acquireDatabaseLock(String lockKey, long timeout) {
        // 模拟实现，实际应使用数据库事务和SELECT FOR UPDATE
        log.warn("数据库锁模拟实现: {}", lockKey);
        return false;
    }
    
    /**
     * 释放数据库悲观锁
     */
    private boolean releaseDatabaseLock(String lockKey) {
        log.warn("数据库锁模拟释放: {}", lockKey);
        return false;
    }
    
    /**
     * 获取内存锁
     * 注意：内存锁仅在单机环境有效，分布式环境应使用Redis或Zookeeper锁
     */
    private boolean acquireMemoryLock(String lockKey, long timeout) {
        // 使用ReentrantLock作为内存锁
        Lock lock = memoryLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());
        log.info("尝试获取内存锁: {}", lockKey);
        try {
            boolean acquired = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            if (acquired) {
                log.info("成功获取内存锁: {}", lockKey);
            } else {
                log.warn("获取内存锁超时: {}", lockKey);
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取内存锁被中断: {}", lockKey, e);
            return false;
        }
    }
    
    /**
     * 释放内存锁
     */
    private boolean releaseMemoryLock(String lockKey) {
        Lock lock = memoryLocks.get(lockKey);
        if (lock != null && lock.tryLock()) { // 尝试获取锁，确保当前线程是锁的拥有者
            try {
                log.info("释放内存锁: {}", lockKey);
                lock.unlock(); // 释放第二次获取的锁
                lock.unlock(); // 释放原始锁
                // 清理不再使用的锁
                memoryLocks.remove(lockKey);
                return true;
            } catch (Exception e) {
                log.error("释放内存锁失败: {}", lockKey, e);
                return false;
            }
        }
        log.warn("内存锁不存在或当前线程不是锁的拥有者: {}", lockKey);
        return false;
    }
}