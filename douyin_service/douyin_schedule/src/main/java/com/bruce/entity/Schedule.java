package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import java.io.Serializable;

/**
 * (Schedule)表实体类
 *
 * @author makejava
 * @since 2025-01-22 00:37:54
 */
@SuppressWarnings("serial")
@Data
public class Schedule extends Model<Schedule> {
    @Id
    private Integer id;
    
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

