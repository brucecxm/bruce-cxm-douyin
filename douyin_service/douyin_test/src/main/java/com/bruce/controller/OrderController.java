package com.bruce.controller;

import com.bruce.dtos.User;
import com.bruce.mapper.UserMapperone;
import com.bruce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderServiceimpl")
    private OrderService orderService;

    // Get mapping for test order selection
    @GetMapping("/test")
    @ResponseBody  // Adding @ResponseBody to return a response body
    public String OrderSelect() {
        orderService.myMethod(); // Call the service method
        return "Order method executed successfully!"; // Returning a success message
    }


    @Autowired
    private UserMapperone userMapper;  // 自动注入 UserMapper


    // Get mapping for test order selection
    @GetMapping("/testone")
    @ResponseBody  // Adding @ResponseBody to return a response body
    public String OrderSelectone() {
        // 第一次查询，走数据库
        User user1 = userMapper.getUserById(1);
        System.out.println("第一次查询：" + user1);

        // 第二次查询，同一个 SqlSession，走一级缓存
        User user2 = userMapper.getUserById(1);
        System.out.println("第二次查询：" + user2);

        return "success";
    }


}
