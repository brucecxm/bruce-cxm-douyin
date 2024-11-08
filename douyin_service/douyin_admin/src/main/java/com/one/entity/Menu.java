package com.one.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2024-11-05 23:56:52
 */
@SuppressWarnings("serial")
public class Menu extends Model<Menu> {
    
    private Integer menuId;
    
    private String menuImg;
    
    private Integer menuName;
    
    private Integer menuType;
    
    private Integer menuLev;
    
    private Integer menuHref;


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    public Integer getMenuName() {
        return menuName;
    }

    public void setMenuName(Integer menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getMenuLev() {
        return menuLev;
    }

    public void setMenuLev(Integer menuLev) {
        this.menuLev = menuLev;
    }

    public Integer getMenuHref() {
        return menuHref;
    }

    public void setMenuHref(Integer menuHref) {
        this.menuHref = menuHref;
    }

}

