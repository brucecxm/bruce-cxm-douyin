package com.bruce.entity;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserData {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    // 添加带参数的构造函数
    public UserData(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    // 可以选择性地添加无参构造函数（Lombok 已经生成）
}
