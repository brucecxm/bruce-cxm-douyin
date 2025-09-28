package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 分页查询店铺数据，支持多条件搜索和排序
     * 
     * @param page 当前页码
     * @param size 每页条数
     * @param shop 店铺查询条件
     * @param sort 排序字段
     * @param order 排序方向（asc/desc）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Map<String, Object> selectAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Shop shop,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "desc") String order) {
        
        // 创建分页对象
        Page<Shop> pageObject = new Page<>(page, size);
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

        // 添加排序功能
        if (StringUtils.hasText(sort)) {
            switch (sort.toLowerCase()) {
                case "shopprice":
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), Shop::getShopPrice);
                    break;
                case "sales":
                    // 如果有销量字段，可以添加销量排序
                    // queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), Shop::getSales);
                    // 目前没有销量字段，默认按价格排序
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), Shop::getShopPrice);
                    break;
                case "shopid":
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), Shop::getShopId);
                    break;
                default:
                    // 默认排序
                    queryWrapper.orderByDesc(Shop::getShopId);
                    break;
            }
        } else {
            // 默认按店铺ID降序排序
            queryWrapper.orderByDesc(Shop::getShopId);
        }

        // 执行分页查询
        IPage<Shop> shopsPage = shopService.page(pageObject, queryWrapper);

        // 封装返回
        Map<String, Object> response = new HashMap<>();
        response.put("shopList", shopsPage.getRecords());
        response.put("total", shopsPage.getTotal());
        response.put("current", shopsPage.getCurrent());
        response.put("size", shopsPage.getSize());
        response.put("pages", shopsPage.getPages());

        return response;
    }



    /**
     * 通过主键查询单条数据
     *
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
    
    /**
     * 分页查询所有店铺数据（简化版）
     * 
     * @param current 当前页码
     * @param size 每页条数
     * @return 分页数据
     */
    @GetMapping
    public R<Page<Shop>> shoplist(@RequestParam Integer current, @RequestParam Integer size) {
        // 创建分页对象
        Page<Shop> page = new Page<>(current, size);
        // 执行分页查询
        Page<Shop> shopPage = shopService.page(page);
        return success(shopPage);
    }
    
    /**
     * 根据关键字模糊查询店铺
     * 
     * @param key 搜索关键字
     * @param current 当前页码
     * @param size 每页条数
     * @return 分页数据
     */
    @GetMapping("/likeall")
    public R<Page<Shop>> getlikeall(@RequestParam String key, @RequestParam Integer current, @RequestParam Integer size) {
        // 创建分页对象
        Page<Shop> page = new Page<>(current, size);
        // 创建查询条件
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊查询店铺名称、描述、信息或类型中包含关键字的记录
        queryWrapper.like(Shop::getShopName, key)
                   .or().like(Shop::getShopDesc, key)
                   .or().like(Shop::getShopInfo, key)
                   .or().like(Shop::getShopType, key);
        
        // 按店铺ID降序排序
        queryWrapper.orderByDesc(Shop::getShopId);
        
        // 执行分页查询
        Page<Shop> shopPage = shopService.page(page, queryWrapper);
        return success(shopPage);
    }
    
    /**
     * 获取店铺分类列表
     * 
     * @return 分类列表
     */
    @GetMapping("/categories")
    public R<List<String>> getCategories() {
        List<String> categories = shopService.getDistinctCategories();
        return success(categories);
    }
}

