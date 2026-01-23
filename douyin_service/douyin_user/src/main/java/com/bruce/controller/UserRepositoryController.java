package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bruce.mapper.UserMapper;

import java.util.Optional;

/**
 * UserRepository Feign 客户端对应的 REST 接口
 * 实现与 UserRepository 方法匹配的接口
 */
@RestController
@RequestMapping("/api/repository/user")
public class UserRepositoryController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 保存用户信息
     * 支持新增和更新操作
     * 
     * @param user 用户信息
     * @return User 保存后的用户信息
     */
    @PostMapping("/save")
    public User save(@RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            // 新增用户
            userMapper.insert(user);
        } else {
            // 更新用户
            userMapper.updateById(user);
        }
        return user;
    }

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findById/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        return Optional.ofNullable(user);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findByUsername")
    public Optional<User> findByUsername(@RequestParam String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        return Optional.ofNullable(user);
    }

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return Optional<User> 用户信息
     */
    @GetMapping("/findByEmail")
    public Optional<User> findByEmail(@RequestParam String email) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
        return Optional.ofNullable(user);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return Boolean 是否存在
     */
    @GetMapping("/existsByUsername")
    public Boolean existsByUsername(@RequestParam String username) {
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        return count > 0;
    }

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return Boolean 是否存在
     */
    @GetMapping("/existsByEmail")
    public Boolean existsByEmail(@RequestParam String email) {
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
        return count > 0;
    }
}