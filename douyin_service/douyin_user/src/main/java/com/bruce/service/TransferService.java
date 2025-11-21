package com.bruce.service;

import com.bruce.entitys.LockType;
import java.math.BigDecimal;

public interface TransferService {
    /**
     * 默认使用Redis锁的转账方法
     */
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);
    
    /**
     * 支持指定锁类型的转账方法
     */
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount, LockType lockType);
}
