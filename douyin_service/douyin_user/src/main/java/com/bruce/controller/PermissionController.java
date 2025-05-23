package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Permission;
import com.bruce.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Permission)表控制层
 *
 * @author makejava
 * @since 2025-05-23 17:34:07
 */
@RestController
@RequestMapping("permission")
public class PermissionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param permission 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Permission> page, Permission permission) {
        return success(this.permissionService.page(page, new QueryWrapper<>(permission)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.permissionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param permission 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Permission permission) {
        return success(this.permissionService.save(permission));
    }

    /**
     * 修改数据
     *
     * @param permission 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Permission permission) {
        return success(this.permissionService.updateById(permission));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.permissionService.removeByIds(idList));
    }
}

