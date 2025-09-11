package com.bruce.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券(Coupon)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-02 19:04:42
 */
@Mapper
public interface CouponDao extends BaseMapper<Coupon> {

}

