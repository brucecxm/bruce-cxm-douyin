package com.bruce.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

/**
 * (Shop)表实体类
 *
 * @author makejava
 * @since 2025-01-19 14:29:41
 */
@SuppressWarnings("serial")
@Mapper
public class Shop extends Model<Shop> {
    
    private Integer shopId;
    
    private String shopName;
    
    private Integer shopPrice;
    
    private String shopDesc;
    
    private String shopImg;
    
    private String shopInfo;


    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Integer shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

}

