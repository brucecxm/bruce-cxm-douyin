package com.bruce.service;

import com.bruce.dtos.Order;

public interface OrderService {

//    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
    // 常量：支付成功的状态码
    public static final int PAYMENT_SUCCESS = 1;
    // 常量：支付失败的状态码
    int PAYMENT_FAILED = 0;  // 默认是 public static final



    // 创建订单
    void createOrder(Order order);

    // 处理订单支付
    void processPayment(Order order);

    // 查询订单
    Order getOrderById(String orderId);

//    // 此方法为protected修饰的  只有子类能调用 我在contrller
//      void test(String address);


    // 接口中的方法是 public
    void myMethod();  // 默认是 public abstract



}
