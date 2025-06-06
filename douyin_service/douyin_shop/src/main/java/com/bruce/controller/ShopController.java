package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Shop;
import com.bruce.service.ShopService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    后端接口返回的 records 有值（对象属性完整），但前端拿到的是 10 个空对象，
//    这是因为 Spring Boot 在接收 Page<Shop> page, Shop shop 参数时，
//    Shop 实体类中有些字段前端没有传，而 Spring 会默认实例化一个空的对象，
//    导致 QueryWrapper 条件未生效或数据反序列化异常。
//    @GetMapping("/page")
//    public R selectAll(Page<Shop> page, Shop shop) {
//LambdaQueryWrapper<Shop> queryWrapper=new LambdaQueryWrapper<>();
////queryWrapper.eq(Shop::)
//        IPage<Shop>  shops=this.shopService.page(page);
//        R one=success(shops);
//        return one;
//    }
//todo 后面发现前面gpt就扯几把蛋呢  最后检查发现是Shop实体类没有加@Date注解  导致序列化异常



    @GetMapping("/page")
    public Map<String, Object> selectAll(Page<Shop> page, Shop shop) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();

        // 模糊查询字段
        if (StringUtils.hasText(shop.getShopName())) {
            queryWrapper.like(Shop::getShopName, shop.getShopName());
        }
        if (StringUtils.hasText(shop.getShopDesc())) {
            queryWrapper.like(Shop::getShopDesc, shop.getShopDesc());
        }
        if (StringUtils.hasText(shop.getShopInfo())) {
            queryWrapper.like(Shop::getShopInfo, shop.getShopInfo());
        }
        if (StringUtils.hasText(shop.getShopType())) {
            queryWrapper.like(Shop::getShopType, shop.getShopType());
        }

        // 精确匹配字段
        if (shop.getShopId() != null) {
            queryWrapper.eq(Shop::getShopId, shop.getShopId());
        }
        if (shop.getShopPrice() != null) {
            queryWrapper.eq(Shop::getShopPrice, shop.getShopPrice());
        }

        // 执行分页查询
        IPage<Shop> shopsPage = shopService.page(page, queryWrapper);

        // 封装返回
        Map<String, Object> response = new HashMap<>();
        response.put("shopList", shopsPage.getRecords());
        response.put("total", shopsPage.getTotal());
        response.put("current", shopsPage.getCurrent());
        response.put("size", shopsPage.getSize());

        return response;
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

