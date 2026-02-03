package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bruce.entity.User;
import com.bruce.pojo.Result;
import com.bruce.service.TransferService;
import com.bruce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/transfer")
@Slf4j
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    
    // 用于跟踪请求，便于问题排查
    private static final AtomicInteger REQUEST_COUNTER = new AtomicInteger(0);

    @PostMapping
    public Result<?> transfer(@RequestParam Long fromUserId,
                                      @RequestParam Long toUserId,
                                      @RequestParam BigDecimal amount,
                                      @RequestParam String password
    ) {
        // 生成请求ID
        int requestId = REQUEST_COUNTER.incrementAndGet();
        log.info("[请求ID:{}] 收到转账请求: fromUserId={}, toUserId={}, amount={}", 
                requestId, fromUserId, toUserId, amount);
        
        try {
            // 参数基本验证
            if (fromUserId == null || toUserId == null || amount == null || password == null) {
                log.warn("[请求ID:{}] 参数错误：存在空值", requestId);
                return Result.error("参数错误：所有参数不能为空");
            }
            
            // 验证转账金额
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("[请求ID:{}] 转账金额无效: {}", requestId, amount);
                return Result.error("转账金额必须大于0");
            }
            
            if (amount.scale() > 2) {
                log.warn("[请求ID:{}] 转账金额精度错误: {}", requestId, amount);
                return Result.error("转账金额最多支持两位小数");
            }
            
            // 验证支付密码
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
            queryWrapper.eq(User::getId, fromUserId);
            User user = userService.getOne(queryWrapper);
            
            if (user == null) {
                log.error("[请求ID:{}] 转出用户不存在: userId={}", requestId, fromUserId);
                return Result.error("转出用户不存在");
            }
            
            if (!password.equals(user.getPaymentPassword())) {
                log.warn("[请求ID:{}] 支付密码错误: userId={}", requestId, fromUserId);
                return Result.error("支付密码错误");
            }
            
            // 执行转账
            log.info("[请求ID:{}] 开始执行转账操作", requestId);
            transferService.transfer(fromUserId, toUserId, amount);
            log.info("[请求ID:{}] 转账成功完成", requestId);
            
            return Result.success("转账成功");
        } catch (Exception e) {
            log.error("[请求ID:{}] 转账失败: error={}", requestId, e.getMessage(), e);
            // 根据异常类型返回不同的错误信息
            if (e.getMessage() != null) {
                return Result.error(e.getMessage());
            } else {
                return Result.error("转账失败，请稍后重试");
            }
        }
    }
}