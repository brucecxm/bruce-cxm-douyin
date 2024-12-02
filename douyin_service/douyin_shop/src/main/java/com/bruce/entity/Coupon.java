package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * 优惠券(Coupon)表实体类
 *
 * @author makejava
 * @since 2024-12-02 19:04:42
 */
@SuppressWarnings("serial")
public class Coupon extends Model<Coupon> {
    
    private Integer id;
    
    private Integer num;
    
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

