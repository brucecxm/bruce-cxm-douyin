package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.dao.VideoOneDao;
import com.bruce.service.VideoOneService;
import com.bruce.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Map> getVideoInfo(int page, int size) {
        int limit = size;
        int offset = (page - 1) * size;
        List<Map> one =videoOneDao.queryVideoInfo(limit, offset);
        return one ;
    }

//    @Override
//    public void insertlianjie(String lianjie) {
//        videoOneDao.insertLianjie(lianjie);
//    }
}

