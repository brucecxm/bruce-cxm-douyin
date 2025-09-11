package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.VideoInfo;

import java.util.Optional;

/**
 * 视频信息表(VideoInfo)表服务接口
 *
 * @author makejava
 * @since 2025-07-15 19:58:31
 */
public interface VideoInfoService extends IService<VideoInfo> {
    // 新增方法定义
    VideoInfo saveVideoInfo(String fileHash, String m3u8Path, String title,
                            Integer duration, Long fileSize, Long userId);
    Optional<VideoInfo> getVideoById(Long id);
    Optional<VideoInfo> getVideoByHash(String fileHash);
}