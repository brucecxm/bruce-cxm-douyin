package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.entity.User;
import com.bruce.mapper.UserMapper;
import com.bruce.service.UserService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author bruce
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public List<User> searchUsersByInfo(String keyword) {
        // 实现根据关键词搜索用户的逻辑
        return baseMapper.selectList(new LambdaQueryWrapper<User>()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getNickname, keyword)
                .last("LIMIT 20"));
    }
    
    @Override
    public User getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }
    
    @Override
    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null) {
            // 实际应用中应该使用密码加密验证
            return user.getPassword().equals(password);
        }
        return false;
    }
    
    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(user);
    }
    
    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = new User();
        user.setId(userId);
        user.setPassword(newPassword);
        return baseMapper.updateById(user) > 0;
    }
}