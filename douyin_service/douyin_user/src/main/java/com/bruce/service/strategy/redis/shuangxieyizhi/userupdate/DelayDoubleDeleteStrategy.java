package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("delayDoubleDelete")
public class DelayDoubleDeleteStrategy implements UserUpdateStrategy {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean updateUserInfo(User user, Long userId) {
        if (user.getId() == null || !user.getId().equals(userId)) {
            throw new IllegalArgumentException("无权修改其他用户信息");
        }

        String cacheKey = "user_info:" + userId;
        String userInfoKey = userId + com.bruce.dict.Constant.User._USER_INFO;

        try {
            stringRedisTemplate.delete(cacheKey);
            stringRedisTemplate.delete(userInfoKey);
        } catch (Exception e) {
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        boolean success = userDao.update(user, updateWrapper) > 0;

        if (success) {
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    stringRedisTemplate.delete(cacheKey);
                    stringRedisTemplate.delete(userInfoKey);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }).start();
        }

        return success;
    }
}
