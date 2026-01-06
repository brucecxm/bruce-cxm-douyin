package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.User;
import com.example.apiserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        logger.info("开始用户注册，请求数据: {}", user);
        try {
            User registeredUser = userService.register(user);
            logger.info("用户注册成功，用户ID: {}", registeredUser.getId());
            return Response.success(registeredUser);
        } catch (Exception e) {
            logger.error("用户注册失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Response<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("开始用户登录，用户名: {}", loginRequest.getUsername());
        try {
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            
            // 生成token（实际项目中应该使用JWT等安全机制）
            String token = UUID.randomUUID().toString().replace("-", "");
            
            // 构建返回数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("user", user);
            responseData.put("token", token);
            
            logger.info("用户登录成功，用户ID: {}, Token已生成", user.getId());
            return Response.success(responseData);
        } catch (Exception e) {
            logger.error("用户登录失败，用户名: {}, 错误: {}", loginRequest.getUsername(), e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    // 登录请求类
    static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        logger.info("开始根据ID查询用户，ID: {}", id);
        try {
            User user = userService.getUserById(id);
            logger.info("根据ID查询用户成功，用户信息: {}", user);
            return Response.success(user);
        } catch (Exception e) {
            logger.error("根据ID查询用户失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Response<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("开始更新用户信息，ID: {}, 更新数据: {}", id, user);
        try {
            User updatedUser = userService.updateUser(id, user);
            logger.info("用户信息更新成功，ID: {}", updatedUser.getId());
            return Response.success(updatedUser);
        } catch (Exception e) {
            logger.error("用户信息更新失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/changePassword")
    public Response<User> changePassword(@RequestParam Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        logger.info("开始修改用户密码，用户ID: {}", id);
        try {
            User updatedUser = userService.changePassword(id, oldPassword, newPassword);
            logger.info("用户密码修改成功，用户ID: {}", id);
            return Response.success(updatedUser);
        } catch (Exception e) {
            logger.error("用户密码修改失败，用户ID: {}, 错误: {}", id, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    // 生成BCrypt密码（仅用于开发测试）
    @GetMapping("/generatePassword")
    public Response<String> generatePassword(@RequestParam String password) {
        logger.info("开始生成BCrypt密码");
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            logger.info("BCrypt密码生成成功");
            return Response.success(encodedPassword);
        } catch (Exception e) {
            logger.error("BCrypt密码生成失败: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
}