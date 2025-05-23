package com.bruce.entity;

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
    
    private Integer shopId;
    
    private String shopName;
    
    private Integer shopPrice;
    
    private String shopDesc;
    
    private String shopImg;
    
    private String shopInfo;


    private String shopType;

}

