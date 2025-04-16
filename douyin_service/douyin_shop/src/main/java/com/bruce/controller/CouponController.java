package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Coupon;
import com.bruce.service.CouponService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 优惠券(Coupon)表控制层
 *
 * @author makejava
 * @since 2025-04-16 13:38:57
 */
@RestController
@RequestMapping("coupon")
public class CouponController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CouponService couponService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param coupon 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Coupon> page, Coupon coupon) {
        return success(this.couponService.page(page, new QueryWrapper<>(coupon)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.couponService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param coupon 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Coupon coupon) {
        return success(this.couponService.save(coupon));
    }

    /**
     * 修改数据
     *
     * @param coupon 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Coupon coupon) {
        return success(this.couponService.updateById(coupon));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.couponService.removeByIds(idList));
    }
}

