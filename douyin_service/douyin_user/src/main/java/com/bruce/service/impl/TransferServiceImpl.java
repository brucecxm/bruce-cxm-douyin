package com.bruce.service.impl;


import com.bruce.entitys.LockType;
import com.bruce.entitys.TransferRecord;
import com.bruce.entitys.Wallet;
import com.bruce.entitys.WalletTransaction;
import com.bruce.mapper.TransferRecordMapper;
import com.bruce.mapper.WalletMapper;
import com.bruce.mapper.WalletTransactionMapper;
import com.bruce.service.DistributedLockService;
import com.bruce.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private TransferRecordMapper transferRecordMapper;
    @Autowired
    private WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private DistributedLockService distributedLockService;

    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        // 默认使用Redis锁
        transfer(fromUserId, toUserId, amount, LockType.REDIS_LOCK);
    }
    
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount, LockType lockType) {
        // 参数校验
        validateTransferParams(fromUserId, toUserId, amount);
        
        // 生成锁键 - 使用用户ID的有序组合，确保同一对用户的转账操作总是使用相同的锁键
        String lockKey = generateLockKey(fromUserId, toUserId);
        boolean lockAcquired = false;
        
        try {
            // 获取锁
            lockAcquired = distributedLockService.acquireLock(lockKey, lockType);
            if (!lockAcquired) {
                log.warn("转账操作获取锁失败: fromUserId={}, toUserId={}, amount={}, lockType={}", 
                         fromUserId, toUserId, amount, lockType);
                throw new RuntimeException("系统繁忙，请稍后重试");
            }
            
            log.info("转账操作获取锁成功: lockKey={}, lockType={}", lockKey, lockType);
            
            // 执行转账操作
            doTransfer(fromUserId, toUserId, amount);
            
        } catch (RuntimeException e) {
            // 记录业务异常
            log.error("转账操作失败: fromUserId={}, toUserId={}, amount={}, error={}", 
                     fromUserId, toUserId, amount, e.getMessage());
            throw e; // 重新抛出异常，让上层处理
        } finally {
            // 释放锁
            if (lockAcquired) {
                boolean releaseSuccess = distributedLockService.releaseLock(lockKey, lockType);
                if (releaseSuccess) {
                    log.info("转账操作释放锁成功: lockKey={}, lockType={}", lockKey, lockType);
                } else {
                    log.error("转账操作释放锁失败: lockKey={}, lockType={}", lockKey, lockType);
                }
            }
        }
    }
    
    /**
     * 执行实际的转账逻辑
     */
    @Transactional
    protected void doTransfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        log.info("开始执行转账事务: fromUserId={}, toUserId={}, amount={}", fromUserId, toUserId, amount);
        
        // 查询钱包信息
        Wallet from = walletMapper.selectByUserId(fromUserId);
        Wallet to = walletMapper.selectByUserId(toUserId);
        
        // 验证钱包状态
        if (from == null) {
            log.error("转出方钱包不存在: userId={}", fromUserId);
            throw new RuntimeException("转出方钱包不存在");
        }
        if (to == null) {
            log.error("转入方钱包不存在: userId={}", toUserId);
            throw new RuntimeException("转入方钱包不存在");
        }
        
        // 验证余额
        if (from.getBalance().compareTo(amount) < 0) {
            log.error("余额不足: userId={}, balance={}, required={}", 
                     fromUserId, from.getBalance(), amount);
            throw new RuntimeException("余额不足");
        }
        
        log.debug("转账前余额检查通过: fromBalance={}, toBalance={}", from.getBalance(), to.getBalance());

        // 扣款/加款
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        
        log.debug("准备更新钱包余额: fromNewBalance={}, toNewBalance={}", from.getBalance(), to.getBalance());
        
        walletMapper.updateById(from);
        walletMapper.updateById(to);
        
        log.info("钱包余额更新成功");

        // 插入转账记录
        TransferRecord record = new TransferRecord();
        record.setFromUserId(fromUserId);
        record.setToUserId(toUserId);
        record.setAmount(amount);
        
        int recordResult = transferRecordMapper.insert(record);
        if (recordResult <= 0) {
            log.error("插入转账记录失败");
            throw new RuntimeException("系统错误，请稍后重试");
        }
        
        log.info("转账记录插入成功: recordId={}", record.getId());

        // 插入交易记录 - 转出
        WalletTransaction out = new WalletTransaction();
        out.setWalletId(from.getId());
        out.setType("TRANSFER_OUT");
        out.setAmount(amount);
        out.setBalanceAfter(from.getBalance());
        out.setDescription("转出给 " + toUserId);
        
        int outResult = walletTransactionMapper.insert(out);
        if (outResult <= 0) {
            log.error("插入转出交易记录失败");
            throw new RuntimeException("系统错误，请稍后重试");
        }

        // 插入交易记录 - 转入
        WalletTransaction in = new WalletTransaction();
        in.setWalletId(to.getId());
        in.setType("TRANSFER_IN");
        in.setAmount(amount);
        in.setBalanceAfter(to.getBalance());
        in.setDescription("来自 " + fromUserId);
        
        int inResult = walletTransactionMapper.insert(in);
        if (inResult <= 0) {
            log.error("插入转入交易记录失败");
            throw new RuntimeException("系统错误，请稍后重试");
        }
        
        log.info("交易记录插入成功");

        try {
            // WebSocket 推送（使用try-catch避免推送失败影响主流程）
            messagingTemplate.convertAndSend("/topic/transfer/" + fromUserId, "你转账了 " + amount);
            messagingTemplate.convertAndSend("/topic/transfer/" + toUserId, "你收到转账 " + amount);
            log.info("WebSocket通知推送成功");
        } catch (Exception e) {
            // 推送失败仅记录日志，不影响主流程
            log.warn("WebSocket通知推送失败: error={}", e.getMessage());
        }
        
        log.info("转账事务执行成功: fromUserId={}, toUserId={}, amount={}", fromUserId, toUserId, amount);
    }
    
    /**
     * 生成锁键
     * 确保同一对用户的转账操作总是使用相同的锁键，避免死锁
     */
    private String generateLockKey(Long userId1, Long userId2) {
        // 为了避免死锁，总是按照用户ID的大小顺序生成锁键
        long smallerId = Math.min(userId1, userId2);
        long largerId = Math.max(userId1, userId2);
        return "transfer_lock:" + smallerId + ":" + largerId;
    }
    
    /**
     * 验证转账参数
     */
    private void validateTransferParams(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId == null || toUserId == null || amount == null) {
            log.error("转账参数为空: fromUserId={}, toUserId={}, amount={}", fromUserId, toUserId, amount);
            throw new RuntimeException("参数错误");
        }
        
        if (fromUserId.equals(toUserId)) {
            log.warn("尝试转账给自己: userId={}", fromUserId);
            throw new RuntimeException("不能转给自己");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("转账金额无效: amount={}", amount);
            throw new RuntimeException("转账金额必须大于0");
        }
        
        // 检查金额精度，假设最多支持两位小数
        if (amount.scale() > 2) {
            log.warn("转账金额精度错误: amount={}", amount);
            throw new RuntimeException("转账金额最多支持两位小数");
        }
        
        log.debug("转账参数验证通过");
    }
}
