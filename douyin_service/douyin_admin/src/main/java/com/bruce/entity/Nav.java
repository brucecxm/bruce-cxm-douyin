package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (Nav)表实体类
 *
 * @author makejava
 * @since 2024-11-06 00:40:08
 */
@SuppressWarnings("serial")
public class Nav extends Model<Nav> {
    
    private Integer navId;
    
    private Integer navName;
    
    private String navImg;
    
    private Integer navHref;
    
    private Integer navType;
    
    private Integer navLev;


    public Integer getNavId() {
        return navId;
    }

    public void setNavId(Integer navId) {
        this.navId = navId;
    }

    public Integer getNavName() {
        return navName;
    }

    public void setNavName(Integer navName) {
        this.navName = navName;
    }

    public String getNavImg() {
        return navImg;
    }

    public void setNavImg(String navImg) {
        this.navImg = navImg;
    }

    public Integer getNavHref() {
        return navHref;
    }

    public void setNavHref(Integer navHref) {
        this.navHref = navHref;
    }

    public Integer getNavType() {
        return navType;
    }

    public void setNavType(Integer navType) {
        this.navType = navType;
    }

    public Integer getNavLev() {
        return navLev;
    }

    public void setNavLev(Integer navLev) {
        this.navLev = navLev;
    }

}

