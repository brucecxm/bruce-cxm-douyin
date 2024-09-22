package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.music.entity.Music;
import com.bruce.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * (Music)表控制层
 *
 * @author makejava
 * @since 2024-06-25 18:10:38
 */
@RestController
@RequestMapping("/music")
public class MusicController extends ApiController {


    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);








    /**
     * 服务对象
     */
    @Resource
    private MusicService musicService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param music 查询实体
     * @return 所有数据
     */
    @GetMapping

    public R selectAll( Page<Music> page, Music music) {
        return success(this.musicService.page(page, new QueryWrapper<>(music)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.musicService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param music 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Music music) {
        return success(this.musicService.save(music));
    }

    /**
     * 修改数据
     *
     * @param music 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Music music) {
        return success(this.musicService.updateById(music));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.musicService.removeByIds(idList));
    }
}

