package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bruce.dao.UserDao;
import com.bruce.dict.Constant;
import com.bruce.entity.User;
import com.bruce.service.UserService;
import com.bruce.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getUserByUsername(String username) {
        return userDao.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", username));
    }

    /**
     * 验证密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 验证结果
     */
    @Override
    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        // 使用MD5加密验证密码
        return Md5Util.getMD5String(password).equals(user.getPassword());
    }

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     */
    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userDao.updateById(user);
    }

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 操作结果
     */
    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = new User();
        user.setId(userId);
        user.setPassword(Md5Util.getMD5String(newPassword));
        return userDao.updateById(user) > 0;
    }

    /**
     * 注册新用户
     * @param username 用户名
     * @param password 密码
     * @return 注册的用户对象
     */
    public User registerNewUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(Md5Util.getMD5String(password));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userDao.insert(user);
        return user;
    }

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userDao.selectById(userId);
        if (user == null) {
            return false;
        }
        // 验证旧密码
        if (!Md5Util.getMD5String(oldPassword).equals(user.getPassword())) {
            return false;
        }
        // 更新密码
        user.setPassword(Md5Util.getMD5String(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        return userDao.updateById(user) > 0;
    }

    /**
     * 根据用户ID列表查询用户信息
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    public List<User> getUserListByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        return userDao.selectBatchIds(userIds);
    }

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新结果
     */
    public boolean updateUserInfo(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        user.setUpdateTime(LocalDateTime.now());
        return userDao.updateById(user) > 0;
    }
    
    /**
     * 根据关键词搜索用户
     * @param keyword 搜索关键词
     * @return 用户列表
     */
    @Override
    public List<User> searchUsersByInfo(String keyword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getEmail, keyword)
                .or()
                .like(User::getPhone, keyword);
        
        // 尝试将关键词转换为Long类型，如果成功则添加ID等于该值的条件
        try {
            Long id = Long.parseLong(keyword);
            wrapper.lambda().or().eq(User::getId, id);
        } catch (NumberFormatException e) {
            // 如果无法转换为Long，则忽略按ID搜索的条件
        }
        
        return userDao.selectList(wrapper);
    }
}