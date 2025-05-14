package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Shop;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Shop)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-19 14:29:41
 */
@Mapper
public interface ShopDao extends BaseMapper<Shop> {

}

