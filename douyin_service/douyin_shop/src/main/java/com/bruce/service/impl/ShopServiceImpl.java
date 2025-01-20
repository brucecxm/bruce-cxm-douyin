package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.ShopDao;
import com.bruce.entity.Shop;
import com.bruce.service.ShopService;
import org.springframework.stereotype.Service;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2025-01-19 14:29:41
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopDao, Shop> implements ShopService {

}

