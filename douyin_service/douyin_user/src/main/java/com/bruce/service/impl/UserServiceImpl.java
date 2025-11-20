package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.entity.User;
import com.bruce.mapper.UserMapper;
import com.bruce.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author bruce
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public List<Long> getHotUserIds(int limit) {
        // 这里实现获取热门用户ID的逻辑
        // 实际应用中，可以根据用户活跃度、粉丝数量等指标来排序并获取热门用户
        // 这里使用简单的模拟实现
        return baseMapper.selectHotUserIds(limit);
    }
    
    @Override
    public User getById(Long userId) {
        return super.getById(userId);
    }
    
    @Override
    public boolean updateById(User user) {
        return super.updateById(user);
    }
}