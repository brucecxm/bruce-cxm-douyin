package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Jingyining)表实体类
 *
 * @author makejava
 * @since 2025-05-12 19:36:31
 */
@SuppressWarnings("serial")
public class Jingyining extends Model<Jingyining> {
    
    private Integer id;
    
    private Integer name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

}

