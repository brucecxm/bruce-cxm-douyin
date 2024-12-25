package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entile.User;
import com.bruce.servid.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;


    @GetMapping("/test")
    public void test() {
        User user=new User();
        user.setAge(188);
        user.setName("Johdn");
        System.out.println("coming");
        userService.createUser(user);
        List<User> userList=new ArrayList<>();
        userList=userService.getAllUsers();
        System.out.println(userList);

    }



}
