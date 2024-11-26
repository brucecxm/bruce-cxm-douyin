package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.FileImg;
import com.bruce.service.FileImgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (FileImg)表控制层
 *
 * @author makejava
 * @since 2024-11-19 17:34:00
 */
@RestController
@RequestMapping("fileImg")
public class FileImgController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private FileImgService fileImgService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param fileImg 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<FileImg> page, FileImg fileImg) {
        return success(this.fileImgService.page(page, new QueryWrapper<>(fileImg)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.fileImgService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param fileImg 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody FileImg fileImg) {
        return success(this.fileImgService.save(fileImg));
    }

    /**
     * 修改数据
     *
     * @param fileImg 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody FileImg fileImg) {
        return success(this.fileImgService.updateById(fileImg));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.fileImgService.removeByIds(idList));
    }
}

