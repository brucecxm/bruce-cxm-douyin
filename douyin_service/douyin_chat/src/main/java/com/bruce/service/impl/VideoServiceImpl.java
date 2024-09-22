package com.bruce.service.impl;

import com.bruce.entity.Video;
import com.bruce.dao.VideoDao;
import com.bruce.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-09-22 15:39:11
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

@Autowired
    private VideoDao videoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param videoId 主键
     * @return 实例对象
     */
    @Override
    public Video queryById(Integer videoId) {
        return this.videoDao.queryById(videoId);
    }

    /**
     * 分页查询
     *
     * @param video 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Video> queryByPage(Video video, PageRequest pageRequest) {
        long total = this.videoDao.count(video);
        return new PageImpl<>(this.videoDao.queryAllByLimit(video, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param video 实例对象
     * @return 实例对象
     */
    @Override
    public Video insert(Video video) {
        this.videoDao.insert(video);
        return video;
    }

    /**
     * 修改数据
     *
     * @param video 实例对象
     * @return 实例对象
     */
    @Override
    public Video update(Video video) {
        this.videoDao.update(video);
        return this.queryById(video.getVideoId());
    }

    /**
     * 通过主键删除数据
     *
     * @param videoId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer videoId) {
        return this.videoDao.deleteById(videoId) > 0;
    }
}
