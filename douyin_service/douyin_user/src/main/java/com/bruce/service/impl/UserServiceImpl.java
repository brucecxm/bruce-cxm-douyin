package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import com.bruce.entity.Salt;
import com.bruce.service.UserService;
import com.bruce.service.SaltService;
import com.bruce.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2024-11-19 11:42:31
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> searchUsersByInfo(String keyword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getEmail, keyword)
                .or()
                .like(User::getPhone, keyword)
                .or()
                .eq(User::getId, keyword);
        return userDao.selectList(wrapper);
    }

    @Override
    public List<Long> listAllUserId() {
        // 使用MyBatis-Plus的lambda查询，只查询ID字段
        return this.listObjs(new LambdaQueryWrapper<User>(), o -> Long.valueOf(o.toString()));
    }

    @Override
    public List<Long> getHotUserIds(int limit) {
        return null;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public boolean updateById(User user) {
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        return userDao.selectOne(wrapper);
    }

    @Autowired
    private SaltService saltService;

    @Override
    public boolean verifyPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        // 根据用户ID获取盐值
        Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, user.getId()));
        if (salt == null) {
            return false;
        }
        // 使用注册时相同的算法进行双重哈希加盐加密
        try {
            // 第一次哈希，使用SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest((password + salt.getSalt()).getBytes());
            // 第二次哈希，使用MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secondHash = md5.digest(firstHash);
            // 转换为Base64编码
            String encryptedPassword = Base64.getEncoder().encodeToString(secondHash);
            // 验证密码是否正确
            return encryptedPassword.equals(user.getPassword());
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userDao.updateById(user);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        // 根据用户ID获取盐值
        Salt salt = saltService.getOne(new LambdaQueryWrapper<Salt>().eq(Salt::getUserId, userId));
        if (salt == null) {
            return false;
        }
        // 使用相同的算法进行双重哈希加盐加密
        try {
            // 第一次哈希，使用SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest((newPassword + salt.getSalt()).getBytes());
            // 第二次哈希，使用MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secondHash = md5.digest(firstHash);
            // 转换为Base64编码
            String encryptedPassword = Base64.getEncoder().encodeToString(secondHash);
            // 加密新密码并更新
            user.setPassword(encryptedPassword);
            return updateById(user);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}

