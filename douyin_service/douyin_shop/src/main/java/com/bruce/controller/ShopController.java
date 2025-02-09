package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Shop;
import com.bruce.service.ShopService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Shop)表控制层
 *
 * @author makejava
 * @since 2025-01-19 14:29:40
 */
@RestController
@RequestMapping("/shop")
public class ShopController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ShopService shopService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param shop 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Shop> page, Shop shop) {
        R one=success(this.shopService.page(page, new QueryWrapper<>(shop)));
        return one;
    }



    @GetMapping("/likeall")
    public R likeall(Page<Shop> page, String key) {

        LambdaQueryWrapper<Shop> lambdaQueryWrapper = new LambdaQueryWrapper<>();
// 查询 name 字段中包含 "张" 的记录
        lambdaQueryWrapper.like(Shop::getShopName, key);

        List<Shop> shop = shopService.list(lambdaQueryWrapper);

        R one=success(shop);
        return one;
    }




    /**
     * 通过主键查询单条数据
     *role
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.shopService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param shop 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Shop shop) {
        return success(this.shopService.save(shop));
    }

    /**
     * 修改数据
     *
     * @param shop 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Shop shop) {
        return success(this.shopService.updateById(shop));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.shopService.removeByIds(idList));
    }
}

