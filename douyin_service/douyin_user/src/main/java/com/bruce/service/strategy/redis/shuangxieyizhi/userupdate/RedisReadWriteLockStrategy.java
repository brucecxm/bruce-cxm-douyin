package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("redisReadWriteLock")
public class RedisReadWriteLockStrategy implements UserUpdateStrategy {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean updateUserInfo(User user, Long userId) {
        if (user.getId() == null || !user.getId().equals(userId)) {
            throw new IllegalArgumentException("无权修改其他用户信息");
        }

        String writeLockKey = "user_write_lock:" + userId;
        String cacheKey = "user_info:" + userId;
        String userInfoKey = userId + com.bruce.dict.Constant.User._USER_INFO;
        boolean lockAcquired = false;

        try {
            lockAcquired = Boolean.TRUE.equals(stringRedisTemplate.opsForValue()
                    .setIfAbsent(writeLockKey, String.valueOf(System.currentTimeMillis()), 30, TimeUnit.SECONDS));

            if (!lockAcquired) {
                throw new RuntimeException("系统繁忙，请稍后重试");
            }

            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", userId);
            boolean success = userDao.update(user, updateWrapper) > 0;

            if (success) {
                try {
                    stringRedisTemplate.delete(cacheKey);
                    stringRedisTemplate.delete(userInfoKey);
                } catch (Exception e) {
                }
            }

            return success;
        } finally {
            if (lockAcquired) {
                try {
                    stringRedisTemplate.delete(writeLockKey);
                } catch (Exception e) {
                }
            }
        }
    }
}
