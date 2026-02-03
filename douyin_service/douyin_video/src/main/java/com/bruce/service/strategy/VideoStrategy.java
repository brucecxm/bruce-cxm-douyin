package com.bruce.service.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bruce.entity.Video;
import com.bruce.vo.VideoVO;

import java.util.List;

/**
 * 视频策略接口
 */
public interface VideoStrategy {
    /**
     * 获取视频列表
     * @param page 页码
     * @param size 每页大小
     * @return 视频列表
     */
    List<VideoVO> getVideos(int page, int size);
}
