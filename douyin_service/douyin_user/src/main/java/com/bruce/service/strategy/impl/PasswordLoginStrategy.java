package com.bruce.service.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.dto.LoginRequestDTO;
import com.bruce.entity.User;
import com.bruce.service.UserService;
import com.bruce.service.strategy.LoginStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.bruce.dict.Constant.User.*;
import static com.bruce.dict.Constant.Error.INVALID_CREDENTIALS;

@Component("password")
public class PasswordLoginStrategy implements LoginStrategy {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User login(LoginRequestDTO requestDTO) {
        String userName = requestDTO.getUserName();
        String password = requestDTO.getPassword();

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        // 热点数据：先从 Redis 查用户
        String userKey = USER_NAME + ":" + userName;
        User user = null;
        try {
            String userJson = stringRedisTemplate.opsForValue().get(userKey);
            if (userJson != null) {
                user = objectMapper.readValue(userJson, User.class);
            } else {
                user = userService.getUserByUsername(userName);
                if (user != null) {
                    // 存入 Redis，10 分钟过期
                    stringRedisTemplate.opsForValue().set(userKey, objectMapper.writeValueAsString(user), 10, TimeUnit.MINUTES);
                }
            }
        } catch (Exception e) {
            // Redis 操作失败，从数据库查询
            user = userService.getUserByUsername(userName);
        }

        if (user == null) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        // 验证密码
        if (!userService.verifyPassword(userName, password)) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }

        return user;
    }
}