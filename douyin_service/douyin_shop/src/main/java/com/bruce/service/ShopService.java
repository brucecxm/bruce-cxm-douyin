package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Shop;

import java.util.List;

/**
 * (Shop)表服务接口
 *
 * @author makejava
 * @since 2025-01-19 14:29:40
 */
public interface ShopService extends IService<Shop> {

    /**
     * 获取店铺分类列表
     * 
     * @return 分类列表
     */
    List<String> getDistinctCategories();
}