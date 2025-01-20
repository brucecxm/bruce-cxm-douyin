package com.bruce.service.impl;

import com.bruce.service.Returnable;
import com.bruce.service.Payable;
import com.bruce.service.Shippable;
//3、接口与抽象类的区别    难度系数：⭐
//        抽象类要被子类继承，接口要被类实现
//        接口可多继承接口，但类只能单继承  ************************
//        抽象类可以有构造器、接口不能有构造器
//        抽象类：除了不能实例化抽象类之外，它和普通Java类没有任何区别
//        抽象类：抽象方法可以有public、protected和default这些修饰符、接口：只能是public
//        抽象类：可以有成员变量；接口：只能声明常量


// 电子产品订单类实现了多个接口
public class ElectronicsOrderimpl implements Payable, Shippable, Returnable {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount);
    }

    @Override
    public void shipOrder(String address) {
        System.out.println("Shipping electronics to " + address);
    }

    @Override
    public void processReturn(int orderId) {
        System.out.println("Processing return for electronics order #" + orderId);
    }
}
