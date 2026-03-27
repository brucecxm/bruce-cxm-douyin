package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("canalAsyncNotify")
public class CanalAsyncNotifyStrategy implements UserUpdateStrategy {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean updateUserInfo(User user, Long userId) {
        if (user.getId() == null || !user.getId().equals(userId)) {
            throw new IllegalArgumentException("无权修改其他用户信息");
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        return userDao.update(user, updateWrapper) > 0;
    }
}
