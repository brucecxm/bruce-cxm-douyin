package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.entity.Shop;
import com.bruce.mapper.ShopMapper;
import com.bruce.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2025-01-19 14:29:40
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    
    @Resource
    private ShopMapper shopMapper;

    /**
     * 获取店铺分类列表
     * 
     * @return 分类列表
     */
    @Override
    public List<String> getDistinctCategories() {
        // 使用自定义SQL或MyBatis-Plus的方法获取不重复的分类列表
        return shopMapper.selectDistinctCategories();
    }
}