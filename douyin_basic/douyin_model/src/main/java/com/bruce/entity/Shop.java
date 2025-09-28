package com.bruce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Shop)表实体类
 *
 * @author makejava
 * @since 2025-01-19 14:29:41
 */
@SuppressWarnings("serial")
@Mapper
@Data
public class Shop extends Model<Shop> {
    
    @TableField("shop_id")
    private Integer shopId;
    
    @TableField("shop_name")
    private String shopName;
    
    @TableField("shop_price")
    private Integer shopPrice;
    
    @TableField("shop_desc")
    private String shopDesc;
    
    @TableField("shop_img")
    private String shopImg;
    
    @TableField("shop_info")
    private String shopInfo;

    @TableField("shop_type")
    private String shopType;

}

