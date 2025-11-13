package com.bruce.listener;
import cn.dev33.satoken.stp.StpUtil;
import com.bruce.pojo.ChatMessageYi;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bruce.config.RabbitConfig.NOTIFY_DELAY_QUEUE;

@Component
public class NotifyMessageConsumer {




    /**
     * 处理延迟消息：根据用户在线状态决定立即推送还是暂存
     */
    @RabbitListener(queues = NOTIFY_DELAY_QUEUE)
    public void handleDelayNotifyMessage(ChatMessageYi message) {

        // 1. 判断用户是否在线（例如：从Redis中查询用户登录状态）
        boolean isOnline =  StpUtil.isLogin(message.getReceiver());

        if (isOnline) {
            System.out.println("用户在线：直接调用第三方服务推送");
            // 2. 用户在线：直接调用第三方服务推送
//            thirdPartyNotifyService.sendNotification(
//                    userId,
//                    message.getContent(),
//                    message.getNotifyType()
//            );
        } else {
//            // 3. 用户不在线：将消息存入数据库/缓存（标记为未读）
//            unreadMessageService.saveUnreadMessage(message);
            // 可选：记录日志（如“用户xxx不在线，消息已暂存”）
            System.out.println("用户不在线：将消息存入数据库");
        }
    }
}
