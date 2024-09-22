package com.bruce.filepojo;

import lombok.Data;

@Data
public class UserData {
    private String name;
    private Integer age;

    public UserData(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public UserData() {
    }
// 省略构造函数、getter 和 setter 方法
}
