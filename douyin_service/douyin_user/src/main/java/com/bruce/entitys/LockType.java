package com.bruce.entitys;

/**
 * 锁类型枚举类
 * 用于定义不同的锁实现机制
 */
public enum LockType {
    
    /**
     * Redis分布式锁（默认锁类型）
     */
    REDIS_LOCK,
    
    /**
     * ZooKeeper分布式锁
     */
    ZOOKEEPER_LOCK,
    
    /**
     * 数据库悲观锁
     */
    DATABASE_LOCK,
    
    /**
     * 内存锁（如ReentrantLock）
     */
    IN_MEMORY_LOCK;
}