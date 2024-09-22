package com.bruce.controller;

import com.bruce.entity.Video;
import com.bruce.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Video)表控制层
 *
 * @author makejava
 * @since 2024-09-22 15:39:08
 */
@RestController
@RequestMapping("video")
public class VideoController {
    /**
     * 服务对象
     */
    @Resource
    private VideoService videoService;

    /**
     * 分页查询
     *
     * @param video 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Video>> queryByPage(Video video, PageRequest pageRequest) {
        return ResponseEntity.ok(this.videoService.queryByPage(video, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Video> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.videoService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param video 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Video> add(Video video) {
        return ResponseEntity.ok(this.videoService.insert(video));
    }

    /**
     * 编辑数据
     *
     * @param video 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Video> edit(Video video) {
        return ResponseEntity.ok(this.videoService.update(video));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.videoService.deleteById(id));
    }

}

