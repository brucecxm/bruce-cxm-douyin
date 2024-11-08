package com.one.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.one.entity.Nav;
import com.one.service.NavService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Nav)表控制层
 *
 * @author makejava
 * @since 2024-11-06 00:40:08
 */
@RestController
@RequestMapping("nav")
public class NavController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private NavService navService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param nav 查询实体
     * @return 所有数据
     */
    @GetMapping("/list")
    public R selectAll(Page<Nav> page, Nav nav) {
        return success(this.navService.page(page, new QueryWrapper<>(nav)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.navService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param nav 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Nav nav) {
        return success(this.navService.save(nav));
    }

    /**
     * 修改数据
     *
     * @param nav 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Nav nav) {
        return success(this.navService.updateById(nav));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.navService.removeByIds(idList));
    }
}

