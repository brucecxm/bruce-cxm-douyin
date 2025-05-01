package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2024-11-05 23:56:52
 */
@SuppressWarnings("serial")
@Data
public class Menu extends Model<Menu> {
    
    private Integer menuId;
    
    private String menuImg;
    
    private String menuName;
    
    private String menuType;
    
    private Integer menuLev;
    
    private Integer menuHref;

}

