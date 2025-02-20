package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (DcSystem)表实体类
 *
 * @author makejava
 * @since 2025-02-20 18:54:14
 */
@SuppressWarnings("serial")
public class DcSystem extends Model<DcSystem> {
    
    private String name;
    
    private String value;
    
    private Integer id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

