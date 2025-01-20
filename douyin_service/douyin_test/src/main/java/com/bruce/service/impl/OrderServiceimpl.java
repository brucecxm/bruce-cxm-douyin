package com.bruce.service.impl;

import com.bruce.dtos.Order;
import com.bruce.service.AbstractOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceimpl extends AbstractOrderService {



    @Override
    public void processPayment(Order order)  {
//        synchronized (lock) {
//            // 先检查库存
//            if (inventoryNotEnough(order)) {
//                System.out.println("库存不足，等待库存更新...");
//                lock.wait(1000);  // 当前线程释放锁，等待1秒
//            }
//
//            // 再次检查库存
//            if (inventoryNotEnough(order)) {
//                System.out.println("库存仍不足，取消订单");
//            } else {
//                process(order);  // 处理订单
//            }
//        }
    }

//    // 模拟库存更新的方法
//    public void updateInventory() {
//        synchronized (lock) {
//            // 更新库存
//            System.out.println("库存已更新，唤醒等待线程");
//            lock.notify();  // 唤醒正在等待的线程
//        }
//    }

//    final 修饰的方法不能被子类重写。
//    final 修饰的方法不能被子类重写。
//    final 修饰的方法不能被子类重写。
//    @Override
//       public Order getOrderById(String orderId) {
//
//    }


    @Override
    public void myMethod() {
        System.out.println("sssss");
    }


    //抽象类可以用protected修饰
    //抽象类可以用protected修饰
    //抽象类可以用protected修饰
    //抽象类可以用protected修饰
    @Override
    protected void test(String address) {

    }
}
