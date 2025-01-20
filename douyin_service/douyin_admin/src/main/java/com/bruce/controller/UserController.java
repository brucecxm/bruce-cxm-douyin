package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.LoginRequest;
import com.bruce.entity.User;
import com.bruce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2024-11-26 17:41:36
 */
@RestController
@RequestMapping("/admin")
public class UserController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;




    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String account=loginRequest.getAccount();

        String password=loginRequest.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, account); // 使用 Lambda 表达式指定查询条件

        User user = userService.getOne(queryWrapper);
        if (user != null && password.equals(user.getPassword())) {
            return ResponseEntity.ok(user);

        }
        return ResponseEntity.status(401).body("用户名或密码错误");




    }



}

