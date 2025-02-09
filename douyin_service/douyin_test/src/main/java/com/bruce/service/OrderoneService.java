package com.bruce.service;


import com.bruce.dtos.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderoneService {

    private List<Order> orderList = new ArrayList<>();

    public List<Order> getAllOrders() {
        return orderList;
    }

    public Order getOrderById(Long id) {
        // 模拟通过ID查找订单
        return null;
    }

    public List<Order> getOrdersByStatus(String status) {
        // 模拟按状态查找订单，假设Order类有status字段（可以自行扩展Order类）
        return orderList.stream()
                .filter(order -> status.equals("completed")) // 这里简单假设返回所有订单
                .collect(Collectors.toList());
    }

    public Order createOrder(Order order) {
        orderList.add(order);
        return order;
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderId().equals(String.valueOf(id))) {
                orderList.set(i, updatedOrder);
                return updatedOrder;
            }
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderList.removeIf(order -> order.getOrderId().equals(String.valueOf(id)));
    }
}
