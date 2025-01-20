package com.bruce.service;

import com.bruce.dtos.Order;

import java.util.HashMap;
import java.util.Map;



//抽象类不能够实例化 只能用来继承
//抽象类不能够实例化 只能用来继承
//抽象类不能够实例化 只能用来继承
//抽象类不能够实例化 只能用来继承

public abstract class AbstractOrderService implements OrderService {


    //    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
//    抽象类：可以有成员变量；接口：只能声明常量
    // 实例变量：订单编号
    protected String orderId;
    // 静态变量：订单总数
    public static int totalOrders = 0;



    // 模拟订单数据库
    private Map<String, Order> orderDatabase = new HashMap<>();
    @Override
    public void createOrder(Order order) {
        // 创建订单的公共逻辑
        orderDatabase.put(order.getOrderId(), order);
        System.out.println("Order " + order.getOrderId() + " created.");
    }

    @Override
    final   public Order getOrderById(String orderId) {
        // 查询订单的公共逻辑
        return orderDatabase.get(orderId);
    }

    // 此方法为protected修饰的  只有子类能调用 我在contrller
    protected    void test(String address)
    {
        System.out.println("ss");
    }



//    当你实现一个接口时，接口中的方法访问权限不能低于接口定义时的访问权限。
//    当你实现一个接口时，接口中的方法访问权限不能低于接口定义时的访问权限。
//    当你实现一个接口时，接口中的方法访问权限不能低于接口定义时的访问权限。
//    当你实现一个接口时，接口中的方法访问权限不能低于接口定义时的访问权限。
//    @Override
//    // 在抽象类中将接口方法的访问权限从 public 改为 protected
//    protected void myMethod() {
//        System.out.println("Method in abstract class");
//    }

}
