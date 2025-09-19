package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.common.R;
import com.bruce.entity.Orders;
import com.bruce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/shop/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        // 设置当前用户ID
        Object userId = StpUtil.getLoginId();
        orders.setUserId((Long) userId);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    
    /**
     * 查询用户订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/list")
    public R<List<Orders>> list(@RequestParam(required = false) Long userId){
        log.info("查询用户订单列表，userId：{}", userId);
        
        // 如果没有传入userId，使用当前登录用户的ID
        if (userId == null) {
            Object loginId = StpUtil.getLoginId();
            userId = Long.parseLong(loginId.toString());
        }
        
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        
        List<Orders> ordersList = orderService.list(queryWrapper);
        return R.success(ordersList);
    }
    
    /**
     * 分页查询用户订单列表
     * @param current 当前页码
     * @param size 每页条数
     * @param status 订单状态（可选）1待付款，2待派送，3已派送，4已完成，5已取消
     * @return 分页订单数据
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam Integer current, 
                                @RequestParam Integer size, 
                                @RequestParam(required = false) Integer status){
        log.info("分页查询用户订单列表，current：{}，size：{}，status：{}", current, size, status);
        
        // 获取当前登录用户的ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.parseLong(loginId.toString());
        
        // 创建分页对象
        Page<Orders> page = new Page<>(current, size);
        
        // 构造查询条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        if (status != null && status > 0) {
            queryWrapper.eq(Orders::getStatus, status);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        
        // 执行分页查询
        Page<Orders> ordersPage = orderService.page(page, queryWrapper);
        return R.success(ordersPage);
    }
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{orderId}")
    public R<String> cancel(@PathVariable Long orderId){
        log.info("取消订单，orderId：{}", orderId);
        
        // 获取当前登录用户的ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.parseLong(loginId.toString());
        
        // 验证订单是否属于当前用户
        Orders orders = orderService.getById(orderId);
        if (orders == null) {
            return R.error("订单不存在");
        }
        
        if (!orders.getUserId().equals(userId)) {
            return R.error("无权操作该订单");
        }
        
        // 检查订单状态，只有待付款状态(1)可以取消
        if (orders.getStatus() != 1) {
            return R.error("当前订单状态不允许取消");
        }
        
        // 更新订单状态为已取消(5)
        orders.setStatus(5);
        orderService.updateById(orders);
        
        return R.success("订单已取消");
    }
    
    /**
     * 确认收货
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/confirm/{orderId}")
    public R<String> confirm(@PathVariable Long orderId){
        log.info("确认收货，orderId：{}", orderId);
        
        // 获取当前登录用户的ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.parseLong(loginId.toString());
        
        // 验证订单是否属于当前用户
        Orders orders = orderService.getById(orderId);
        if (orders == null) {
            return R.error("订单不存在");
        }
        
        if (!orders.getUserId().equals(userId)) {
            return R.error("无权操作该订单");
        }
        
        // 检查订单状态，只有待收货状态(3)可以确认收货
        if (orders.getStatus() != 3) {
            return R.error("当前订单状态不允许确认收货");
        }
        
        // 更新订单状态为已完成(4)
        orders.setStatus(4);
        orderService.updateById(orders);
        
        return R.success("已确认收货");
    }
    
    /**
     * 根据订单ID查询订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/getById/{orderId}")
    public R<Orders> getById(@PathVariable Long orderId){
        log.info("查询订单详情，orderId：{}", orderId);
        
        // 获取当前登录用户的ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.parseLong(loginId.toString());
        
        // 验证订单是否属于当前用户
        Orders orders = orderService.getById(orderId);
        if (orders == null) {
            return R.error("订单不存在");
        }
        
        if (!orders.getUserId().equals(userId)) {
            return R.error("无权查看该订单");
        }
        
        return R.success(orders);
    }
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param statusInfo 状态信息
     * @return 操作结果
     */
    @PostMapping("/updateStatus/{orderId}")
    public R<String> updateStatus(@PathVariable Long orderId, @RequestBody StatusInfo statusInfo){
        log.info("更新订单状态，orderId：{}，新状态：{}", orderId, statusInfo.getStatus());
        
        // 获取当前登录用户的ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.parseLong(loginId.toString());
        
        // 验证订单是否属于当前用户
        Orders orders = orderService.getById(orderId);
        if (orders == null) {
            return R.error("订单不存在");
        }
        
        if (!orders.getUserId().equals(userId)) {
            return R.error("无权操作该订单");
        }
        
        // 验证状态转换的合理性
        Integer currentStatus = orders.getStatus();
        Integer newStatus = statusInfo.getStatus();
        
        // 状态转换规则：
        // 1(待付款) -> 2(待发货) 或 5(已取消)
        // 2(待发货) -> 3(已派送)
        // 3(已派送) -> 4(已完成)
        // 其他状态转换需要根据业务规则进行验证
        if (currentStatus == 1 && newStatus == 2) {
            // 待付款 -> 待发货（支付成功）
            orders.setStatus(newStatus);
        } else if (currentStatus == 1 && newStatus == 5) {
            // 待付款 -> 已取消（用户取消订单）
            orders.setStatus(newStatus);
        } else if (currentStatus == 3 && newStatus == 4) {
            // 已派送 -> 已完成（用户确认收货）
            orders.setStatus(newStatus);
        } else {
            return R.error("当前订单状态不允许进行此操作");
        }
        
        orderService.updateById(orders);
        return R.success("订单状态更新成功");
    }
    
    // 内部静态类，用于接收状态更新请求参数
    public static class StatusInfo {
        private Integer status;
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}