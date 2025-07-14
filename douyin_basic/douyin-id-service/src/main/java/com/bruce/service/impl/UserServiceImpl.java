package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserDao;
import com.bruce.entity.User;
import com.bruce.mapper.UserMapper;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

