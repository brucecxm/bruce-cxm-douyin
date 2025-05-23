package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.entity.Menu;
import com.bruce.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Menu)表控制层
 *
 * @author makejava
 * @since 2024-11-05 23:56:50
 */
@RestController
@RequestMapping("/admin/menu")
public class MenuController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;










    @PostMapping("/list")
    public R selectAll(@RequestBody Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        // menuType 是 String
        queryWrapper.eq(menu.getMenuType() != null && !menu.getMenuType().isEmpty(), Menu::getMenuType, menu.getMenuType());

        // menuName 是 String（示例）
        queryWrapper.eq(menu.getMenuName() != null && !menu.getMenuName().isEmpty(), Menu::getMenuName, menu.getMenuName());

        // menuId 是 Integer
        queryWrapper.eq(menu.getMenuId() != null, Menu::getMenuId, menu.getMenuId());

        // menuLev 是 Integer
        queryWrapper.eq(menu.getMenuLev() != null, Menu::getMenuLev, menu.getMenuLev());

        // menuHref 是 Integer
        queryWrapper.eq(menu.getMenuHref() != null, Menu::getMenuHref, menu.getMenuHref());

        // status 是 int 基本类型，假设0表示不筛选，非0时筛选
        if (menu.getStatus() != 0) {
            queryWrapper.eq(Menu::getStatus, menu.getStatus());
        }

        List<Menu> menus = this.menuService.list(queryWrapper);
        return success(menus);
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.menuService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Menu menu) {
        return success(this.menuService.save(menu));
    }

    /**
     * 修改数据
     *
     * @param menu 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Menu menu) {
        return success(this.menuService.updateById(menu));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.menuService.removeByIds(idList));
    }
}

