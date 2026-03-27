package com.bruce.service.strategy.redis.shuangxieyizhi.userupdate;

import com.bruce.entity.User;

public interface UserUpdateStrategy {
    boolean updateUserInfo(User user, Long userId);
}
