package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Order;
import com.example.apiserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Response<Order> createOrder(@RequestBody OrderService.OrderRequest orderRequest) {
        try {
            Order createdOrder = orderService.createOrder(orderRequest);
            return Response.success("订单创建成功", createdOrder);
        } catch (Exception e) {
            return Response.error("订单创建失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Response<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return Response.success(orders);
        } catch (Exception e) {
            return Response.error("获取订单列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Response<Order> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            return Response.success(order);
        } catch (Exception e) {
            return Response.error("获取订单详情失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Response<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return Response.success("订单状态更新成功", updatedOrder);
        } catch (Exception e) {
            return Response.error("订单状态更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return Response.<Void>success("订单删除成功", null);
        } catch (Exception e) {
            return Response.error("订单删除失败: " + e.getMessage());
        }
    }
}