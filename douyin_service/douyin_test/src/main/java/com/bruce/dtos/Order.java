package com.bruce.dtos;

import lombok.Data;

@Data //data注解会自动为你生生成一个空参的构造函数  会造成后面的调用构造方法签名不正确 因此要自己创建一个构造方法
public class Order {


String OrderId;
    Product product;
    // 手动添加带有参数的构造方法
    //data注解会自动为你生生成一个空参的构造函数  会造成后面的调用构造方法签名不正确 因此要自己创建一个构造方法
    //data注解会自动为你生生成一个空参的构造函数  会造成后面的调用构造方法签名不正确 因此要自己创建一个构造方法
    //data注解会自动为你生生成一个空参的构造函数  会造成后面的调用构造方法签名不正确 因此要自己创建一个构造方法
    //data注解会自动为你生生成一个空参的构造函数  会造成后面的调用构造方法签名不正确 因此要自己创建一个构造方法
    public Order(String orderId, Product product) {
        this.OrderId = orderId;
        this.product = product;
    }







//    浅拷贝的存在意义：
// 创建一个商品对象
//Product product = new Product("Laptop", 999.99);
//
//    // 创建两个订单对象，指向同一个商品对象
//    Order order1 = new Order("ORD123", product);
//    Order order2 = order1.shallowCopy();
//
//    // 修改 order1 中商品的价格
//    order1.product.price = 1099.99;
//
//    // 打印订单信息
//        System.out.println("After price update in Order1:");
//        order1.displayOrderDetails();  // Order 1: Laptop, 1099.99
//        order2.displayOrderDetails();  // Order 2: Laptop, 1099.99
//    浅拷贝适用于当你希望多个对象共享同一资源时。电商系统中，经常会遇到需要多个订单共享同一商品信息的场景，浅拷贝非常适用。
//    订单1 和 订单2 都包含同一个 Laptop 商品对象，在浅拷贝中这两个订单只会共享商品的引用，修改其中一个订单的商品价格会影响到另一个订单的商品价格。
    // 浅拷贝方法
    public Order shallowCopy() {
        return new Order(this.OrderId, this.product);  // 只拷贝引用
    }








//
//    深拷贝的存在意义：
//    数据独立性与避免意外修改：
// 创建一个商品对象
//Product product = new Product("Laptop", 999.99);
//
//    // 创建两个订单对象，分别包含独立的商品副本
//    Order order1 = new Order("ORD123", product);
//    Order order2 = order1.deepCopy();  // 使用深拷贝，创建独立的商品副本
//
//    // 修改 order1 中商品的价格
//    order1.product.price = 1099.99;
//
//    // 打印订单信息
//        System.out.println("After price update in Order1:");
//        order1.displayOrderDetails();  // Order 1: Laptop, 1099.99
//        order2.displayOrderDetails();  // Order 2: Laptop, 999.99
//    订单1 和 订单2 分别包含独立的 Laptop 商品对象，在深拷贝中这两个订单分别拥有各自独立的商品对象，修改其中一个订单的商品价格不会影响另一个订单的商品价格。
    // 深拷贝方法
    public Order deepCopy() {
        // 创建一个新的 Product 对象，并复制字段
        Product newProduct = new Product(this.product.name, this.product.price);
        return new Order(this.OrderId, newProduct);  // 拷贝的是新对象
    }
}
