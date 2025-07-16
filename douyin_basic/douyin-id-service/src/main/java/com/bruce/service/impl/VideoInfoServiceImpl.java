package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoInfoDao;
import com.bruce.entity.VideoInfo;
import com.bruce.service.VideoInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
/**
 * 视频信息表(VideoInfo)表服务实现类
 *
 * @author makejava
 * @since 2025-07-15 19:58:31
 */
@Service("videoInfoService")
public class VideoInfoServiceImpl extends ServiceImpl<VideoInfoDao, VideoInfo> implements VideoInfoService {

    // 保存视频信息到数据库
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoInfo saveVideoInfo(String fileHash, String m3u8Path, String title,
                                   Integer duration, Long fileSize, Long userId) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setFileHash(fileHash);
        videoInfo.setM3u8MinioPath(m3u8Path);
        videoInfo.setVideoTitle(title);
        videoInfo.setDuration(duration);
        videoInfo.setFileSize(fileSize);
        videoInfo.setUserId(userId);
        videoInfo.setStatus(1); // 状态：可用

        // 使用MyBatis-Plus的save方法保存实体
        this.save(videoInfo);
        return videoInfo;
    }

    // 根据ID获取视频信息
    @Override
    public Optional<VideoInfo> getVideoById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    // 根据文件哈希获取视频信息
    @Override
    public Optional<VideoInfo> getVideoByHash(String fileHash) {
        return this.lambdaQuery()
                .eq(VideoInfo::getFileHash, fileHash)
                .oneOpt();
    }
}