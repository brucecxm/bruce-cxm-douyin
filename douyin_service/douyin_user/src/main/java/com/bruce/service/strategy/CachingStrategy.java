package com.bruce.service.strategy;

import com.bruce.entity.User;
import com.bruce.entity.UserInfo;

public interface CachingStrategy {
    /**
     * 根据用户ID获取用户信息，使用特定的缓存策略
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 用户信息
     */
    UserInfo getUserInfo(Long userId, Integer page, Integer pageSize);
}
