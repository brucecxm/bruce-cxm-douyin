package com.bruce.Listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.Wallet;
import com.bruce.entity.WalletDetail;
import com.bruce.service.WalletDetailService;
import com.bruce.service.WalletService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class create_wallet_listener {
    @Autowired
    private WalletDetailService walletDetailService;

    @Autowired
    private WalletService walletService;

    // 监听队列 "exampleQueue"
    @RabbitListener(queues = "create_wallet_queue")
    public void receiveMessage(String userid) {
        System.out.println("接收到消息：" + userid);

        Wallet wallet = new Wallet();
        WalletDetail walletDetail = new WalletDetail();
        wallet.setUserid(Integer.valueOf(userid));
        wallet.setAmount(0);

        walletService.save(wallet);
        LambdaQueryWrapper<Wallet> query = new LambdaQueryWrapper();
        query.eq(Wallet::getUserid, userid);
        Wallet wallet1 = walletService.getOne(query);
        int wallet_id = wallet1.getWalletId();
        walletDetail.setWalletId(wallet_id);
        walletDetail.setType("create");
        walletDetail.setUserId(Integer.valueOf(userid));
        walletDetailService.save(walletDetail);


    }
}
