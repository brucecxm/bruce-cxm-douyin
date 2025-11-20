package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.User;
import com.bruce.service.TransferService;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/wallet")
public class WalletController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransferService transferService;

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


    /**
     * 检查钱包状态
     * @return 钱包状态信息
     */
    @GetMapping("/status")
    public ResponseEntity<?> checkWalletStatus() {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }

            // 检查用户是否设置了支付密码，如果设置了则表示开通了钱包
            boolean walletEnabled = user.getPaymentPassword() != null && !user.getPaymentPassword().isEmpty();

            Map<String, Object> response = new HashMap<>();
            response.put("enabled", walletEnabled);

            // 如果开通了钱包，返回余额信息
            if (walletEnabled) {
                // 在实际项目中，余额应该从钱包表中获取
                // 这里使用模拟数据
                response.put("balance", user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 开通钱包
     * @param paymentPassword 支付密码
     * @return 开通结果
     */
    @PostMapping("/enable")
    public ResponseEntity<?> enableWallet(@RequestParam String paymentPassword) {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }

            // 验证支付密码格式（6位数字）
            if (paymentPassword == null || paymentPassword.length() != 6 || !paymentPassword.matches("\\d{6}")) {
                return ResponseEntity.badRequest().body("请输入6位数字支付密码");
            }

            // 设置支付密码并初始化余额
            user.setPaymentPassword(paymentPassword);
            if (user.getBalance() == null) {
                user.setBalance(BigDecimal.ZERO);
            }

            userService.updateById(user);

            return ResponseEntity.ok("钱包开通成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取钱包余额
     * @return 余额信息
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getWalletBalance() {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("balance", user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
