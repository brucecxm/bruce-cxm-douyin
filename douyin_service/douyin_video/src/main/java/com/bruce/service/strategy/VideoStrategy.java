package com.bruce.service.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bruce.entity.Video;

import java.util.List;
import java.util.Map;

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
    List<Map<String, Object>> getVideos(int page, int size);
}
