package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.User;
import com.bruce.entitys.WalletTransaction;
import com.bruce.mapper.WalletTransactionMapper;
import com.bruce.pojo.Result;
import com.bruce.service.TransferService;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private WalletTransactionMapper walletTransactionMapper;



    /**
     * 检查钱包状态
     * @return 钱包状态信息
     */
    @GetMapping("/status")
    public Result<?> checkWalletStatus() {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
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

            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 开通钱包
     * @param paymentPassword 支付密码
     * @return 开通结果
     */
    @PostMapping("/enable")
    public Result<?> enableWallet(@RequestParam String paymentPassword) {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证支付密码格式（6位数字）
            if (paymentPassword == null || paymentPassword.length() != 6 || !paymentPassword.matches("\\d{6}")) {
                return Result.error("请输入6位数字支付密码");
            }

            // 设置支付密码并初始化余额
            user.setPaymentPassword(paymentPassword);
            if (user.getBalance() == null) {
                user.setBalance(BigDecimal.ZERO);
            }

            userService.updateById(user);

            return Result.success("钱包开通成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取钱包余额
     * @return 余额信息
     */
    @GetMapping("/balance")
    public Result<?> getWalletBalance() {
        try {
            // 在实际项目中，这里应该从session或token中获取用户ID
            // 为了演示，这里假设用户ID为1
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());


            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("balance", user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);

            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取钱包交易记录
     * @param page 页码
     * @param pageSize 每页大小
     * @return 交易记录列表
     */
    @GetMapping("/transactions")
    public Result<?> getWalletTransactions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            // 获取当前登录用户ID
            Long userId = Long.parseLong(StpUtil.getLoginId().toString());

            // 这里假设walletId等于userId，实际项目中可能需要从wallet表中查询
            Long walletId = userId;

            // 构建分页查询条件
            Page<WalletTransaction> pageInfo = new Page<>(page, pageSize);
            LambdaQueryWrapper<WalletTransaction> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WalletTransaction::getWalletId, walletId)
                       .orderByDesc(WalletTransaction::getCreateTime);

            // 执行查询
            Page<WalletTransaction> result = walletTransactionMapper.selectPage(pageInfo, queryWrapper);

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("records", result.getRecords());
            response.put("total", result.getTotal());
            response.put("page", result.getCurrent());
            response.put("pageSize", result.getSize());

            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
