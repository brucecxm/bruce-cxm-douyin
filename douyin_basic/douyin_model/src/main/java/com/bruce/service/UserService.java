package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.User;
import java.util.List;

/**
 * 用户服务接口
 *
 * @author bruce
 */
public interface UserService extends IService<User> {
    
    /**
     * 获取热门用户ID列表
     * @param limit 获取的用户数量限制
     * @return 热门用户ID列表
     */
    List<Long> getHotUserIds(int limit);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User getById(Long userId);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否更新成功
     */
    boolean updateById(User user);
}