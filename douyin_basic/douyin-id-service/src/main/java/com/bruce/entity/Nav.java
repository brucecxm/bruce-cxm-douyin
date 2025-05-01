package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (Nav)表实体类
 *
 * @author makejava
 * @since 2024-11-06 00:40:08
 */
@Data
@SuppressWarnings("serial")
public class Nav extends Model<Nav> {
    
    private Integer navId;
    
    private String navName;
    
    private String navImg;
    
    private Integer navHref;
    
    private String navType;
    
    private Integer navLev;


    public Integer getNavId() {
        return navId;
    }



}

