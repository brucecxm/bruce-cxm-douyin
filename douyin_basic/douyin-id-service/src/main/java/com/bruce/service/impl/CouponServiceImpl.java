package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.CouponDao;
import com.bruce.entity.Coupon;
import com.bruce.service.CouponService;
import org.springframework.stereotype.Service;

/**
 * 优惠券(Coupon)表服务实现类
 *
 * @author makejava
 * @since 2024-12-02 19:04:42
 */
@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponDao, Coupon> implements CouponService {

}

