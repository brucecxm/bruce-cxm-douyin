package com.bruce.utils;

import com.bruce.dtos.Order;
import org.springframework.stereotype.Service;

@Service
public class RedisUtils {


    public void putOrder(Order order) {
        //常规的 如果order为空 一般不进行缓存  但是因为缓存穿透的因素  这里可以改为 如果是空 也缓存数据
        if(order==null)
        {

        }

        System.out.println(order);
    }

    public Order getOrderById(Long id) {
        // 模拟通过ID查找订单
        return null;
    }



}
