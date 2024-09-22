package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.dao.VideoOneDao;
import com.bruce.service.VideoOneService;
import com.bruce.service.VideoService;
import com.bruce.video.entity.Video;
import com.bruce.video.entity.VideoOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
@Service("videoOneService")
public class VideoOneServiceImpl implements VideoOneService {



    @Autowired
    private VideoOneDao videoOneDao;


    @Override
    public List<VideoOne> queryVideoInfo(int videoId) {

        return videoOneDao.queryVideoInfo(videoId);
    }
}

