package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("mqAsyncNotify")
public class MqAsyncNotifyStrategy implements UserUpdateStrategy {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public boolean updateUserInfo(User user, Long userId) {
        if (user.getId() == null || !user.getId().equals(userId)) {
            throw new IllegalArgumentException("无权修改其他用户信息");
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        boolean success = userDao.update(user, updateWrapper) > 0;

        if (success) {
            try {
                Map<String, Object> message = new HashMap<>();
                message.put("userId", userId);
                message.put("updateTime", System.currentTimeMillis());
                rabbitTemplate.convertAndSend("user.update.queue", message);
            } catch (Exception e) {
            }
        }

        return success;
    }
}
