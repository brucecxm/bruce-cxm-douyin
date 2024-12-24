package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.video.entity.Video;
import com.bruce.video.entity.VideoOne;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (Video)表服务接口
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
public interface VideoOneService {
 List<Map> getVideoInfo(int page, int size);
}

