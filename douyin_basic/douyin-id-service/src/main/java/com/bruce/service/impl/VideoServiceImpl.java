package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.entity.Video;
import com.bruce.service.VideoService;
import org.springframework.stereotype.Service;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {


}

