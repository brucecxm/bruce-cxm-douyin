package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Shop;

import java.util.List;

/**
 * (Shop)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-19 14:29:40
 */
public interface ShopMapper extends BaseMapper<Shop> {

    /**
     * 获取不重复的店铺分类列表
     * 
     * @return 分类列表
     */
    List<String> selectDistinctCategories();
}