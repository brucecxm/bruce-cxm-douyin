package com.bruce.service.strategy.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.dto.LoginRequestDTO;
import com.bruce.entity.User;
import com.bruce.service.UserService;
import com.bruce.service.strategy.login.LoginStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.bruce.dict.Constant.User.*;
import static com.bruce.dict.Constant.Error.CAPTCHA_ERROR;

@Component("email")
public class EmailLoginStrategy implements LoginStrategy {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User login(LoginRequestDTO requestDTO) {
        String userName = requestDTO.getUserName();
        String emailVerificationCode = requestDTO.getEmailVerificationCode();

        if (emailVerificationCode == null || emailVerificationCode.isEmpty()) {
            throw new IllegalArgumentException("邮箱验证码不能为空");
        }

        // 验证邮箱验证码
        String code = stringRedisTemplate.opsForValue().get(EMAIL_CODE_PREFIX + userName);
        if (code == null || !emailVerificationCode.equals(code)) {
            throw new IllegalArgumentException(CAPTCHA_ERROR);
        }

        // 热点数据：先从 Redis 查用户
        String userKey = USER_NAME + ":" + userName;
        User user = null;
        try {
            String userJson = stringRedisTemplate.opsForValue().get(userKey);
            if (userJson != null) {
                user = objectMapper.readValue(userJson, User.class);
            } else {
                user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
                if (user != null) {
                    // 存入 Redis，10 分钟过期
                    stringRedisTemplate.opsForValue().set(userKey, objectMapper.writeValueAsString(user), 10, TimeUnit.MINUTES);
                }
            }
        } catch (Exception e) {
            // Redis 操作失败，从数据库查询
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        }

        if (user == null) {
            // 用户不存在则自动注册，随机8位密码可用于后续找回
            String randomPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            // 这里需要调用注册方法，暂时抛出异常，后续在 UserService 中实现
            throw new UnsupportedOperationException("自动注册功能暂未实现");
        }

        return user;
    }
}
