package com.bruce.service;

import com.bruce.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Video)表服务接口
 *
 * @author makejava
 * @since 2024-09-22 15:39:11
 */
public interface VideoService {

    /**
     * 通过ID查询单条数据
     *
     * @param videoId 主键
     * @return 实例对象
     */
    Video queryById(Integer videoId);

    /**
     * 分页查询
     *
     * @param video 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Video> queryByPage(Video video, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param video 实例对象
     * @return 实例对象
     */
    Video insert(Video video);

    /**
     * 修改数据
     *
     * @param video 实例对象
     * @return 实例对象
     */
    Video update(Video video);

    /**
     * 通过主键删除数据
     *
     * @param videoId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer videoId);

}
