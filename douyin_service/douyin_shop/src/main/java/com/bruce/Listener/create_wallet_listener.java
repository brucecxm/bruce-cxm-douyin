package com.bruce.Listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.Wallet;
import com.bruce.entity.WalletDetail;
import com.bruce.service.WalletDetailService;
import com.bruce.service.WalletService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import com.rabbitmq.client.Channel;

@Service
public class create_wallet_listener {

    @Autowired
    private WalletDetailService walletDetailService;

    @Autowired
    private WalletService walletService;

    // 监听队列 "create_walletone_queue"
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "create_walletone_queue"),
            exchange = @Exchange(name = "create_walletone_exampleExchange"),
            key = {"create_walletone_RoutingKey"}
    ))
    public void receiveMessage(Message message, Channel channel) {
        String userid = new String(message.getBody());  // 获取消息内容
        System.out.println("接收到消息：" + userid);

        try {
            // 执行业务逻辑
            Wallet wallet = new Wallet();
            WalletDetail walletDetail = new WalletDetail();
            wallet.setUserid(Integer.valueOf(userid));
            wallet.setAmount(0);

            walletService.save(wallet);

            LambdaQueryWrapper<Wallet> query = new LambdaQueryWrapper<>();
            query.eq(Wallet::getUserid, userid);
            Wallet wallet1 = walletService.getOne(query);
            int wallet_id = wallet1.getWalletId();
            walletDetail.setWalletId(wallet_id);
            walletDetail.setType("create");
            walletDetail.setUserId(Integer.valueOf(userid));
            walletDetailService.save(walletDetail);

            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);  // 确认消息已处理
        } catch (Exception e) {
            e.printStackTrace();

            // 如果处理失败，可以选择拒绝消息，并且不重新入队
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);  // 拒绝消息
            } catch (Exception ackEx) {
                ackEx.printStackTrace();
            }
        }
    }
}
