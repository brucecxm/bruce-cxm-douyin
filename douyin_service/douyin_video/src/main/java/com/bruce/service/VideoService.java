package com.bruce.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Video;

import java.util.List;
import java.util.Map;

/**
 * (Video)表服务接口
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
public interface VideoService extends IService<Video> {

     List<Map<String, Object>> getVideoPage(int page,
                                                   int size,
                                                   String videoType);


}

