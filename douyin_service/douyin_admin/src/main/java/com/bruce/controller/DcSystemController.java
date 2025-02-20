package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.DcSystem;
import com.bruce.service.DcSystemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (DcSystem)表控制层
 *
 * @author makejava
 * @since 2025-02-20 18:54:14
 */
@RestController
@RequestMapping("dcSystem")
public class DcSystemController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private DcSystemService dcSystemService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dcSystem 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<DcSystem> page, DcSystem dcSystem) {
        return success(this.dcSystemService.page(page, new QueryWrapper<>(dcSystem)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.dcSystemService.getById(id));
    }






    /**
     * 新增数据
     *
     * @param dcSystem 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody DcSystem dcSystem) {
        return success(this.dcSystemService.save(dcSystem));
    }

    /**
     * 修改数据
     *
     * @param dcSystem 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody DcSystem dcSystem) {
        return success(this.dcSystemService.updateById(dcSystem));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.dcSystemService.removeByIds(idList));
    }
}

