package com.bruce.dtos;

import lombok.Data;

// 商品类
@Data
class Product {
    String name;
    double price;



    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
