package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.User;
import com.bruce.service.TransferService;
import com.bruce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestParam Long fromUserId,
                                      @RequestParam Long toUserId,
                                      @RequestParam BigDecimal amount,
                                      @RequestParam String password
    ) {
        try {
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getId,fromUserId);
       User user= userService.getOne(queryWrapper);
       if(!user.getPaymentPassword().equals(password))
       {
           return ResponseEntity.badRequest().body("支付密码错误");
       }
            transferService.transfer(fromUserId, toUserId, amount);
            return ResponseEntity.ok("成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}