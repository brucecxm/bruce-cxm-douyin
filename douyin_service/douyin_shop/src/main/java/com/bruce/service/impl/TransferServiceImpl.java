package com.bruce.service.impl;

import com.bruce.entitys.TransferRecord;
import com.bruce.entitys.Wallet;
import com.bruce.entitys.WalletTransaction;
import com.bruce.mapper.TransferRecordMapper;
import com.bruce.mapper.WalletMapper;
import com.bruce.mapper.WalletTransactionMapper;
import com.bruce.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    @Autowired
    private  WalletMapper walletMapper;
    @Autowired
    private  TransferRecordMapper transferRecordMapper;
    @Autowired
    private  WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private  SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) throw new RuntimeException("不能转给自己");

        Wallet from = walletMapper.selectByUserId(fromUserId);
        Wallet to = walletMapper.selectByUserId(toUserId);
        if (from == null || to == null) throw new RuntimeException("钱包不存在");
        if (from.getBalance().compareTo(amount) < 0) throw new RuntimeException("余额不足");

        // 扣款/加款
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        walletMapper.updateById(from);
        walletMapper.updateById(to);

        // 插入转账记录
        TransferRecord record = new TransferRecord();
        record.setFromUserId(fromUserId);
        record.setToUserId(toUserId);
        record.setAmount(amount);
        transferRecordMapper.insert(record);

        // 插入交易记录
        WalletTransaction out = new WalletTransaction();
        out.setWalletId(from.getId());
        out.setType("TRANSFER_OUT");
        out.setAmount(amount);
        out.setBalanceAfter(from.getBalance());
        out.setDescription("转出给 " + toUserId);
        walletTransactionMapper.insert(out);

        WalletTransaction in = new WalletTransaction();
        in.setWalletId(to.getId());
        in.setType("TRANSFER_IN");
        in.setAmount(amount);
        in.setBalanceAfter(to.getBalance());
        in.setDescription("来自 " + fromUserId);
        walletTransactionMapper.insert(in);

        // WebSocket 推送
        messagingTemplate.convertAndSend("/topic/transfer/" + fromUserId, "你转账了 " + amount);
        messagingTemplate.convertAndSend("/topic/transfer/" + toUserId, "你收到转账 " + amount);
    }
}