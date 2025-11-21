package com.bruce.service;

import com.bruce.entitys.LockType;

/**
 * 分布式锁服务接口
 * 提供获取和释放锁的功能
 */
public interface DistributedLockService {
    
    /**
     * 获取锁
     * @param lockKey 锁的键
     * @param lockType 锁类型
     * @return 是否成功获取锁
     */
    boolean acquireLock(String lockKey, LockType lockType);
    
    /**
     * 获取锁（带超时时间）
     * @param lockKey 锁的键
     * @param lockType 锁类型
     * @param timeout 超时时间（毫秒）
     * @return 是否成功获取锁
     */
    boolean acquireLock(String lockKey, LockType lockType, long timeout);
    
    /**
     * 释放锁
     * @param lockKey 锁的键
     * @param lockType 锁类型
     * @return 是否成功释放锁
     */
    boolean releaseLock(String lockKey, LockType lockType);
}