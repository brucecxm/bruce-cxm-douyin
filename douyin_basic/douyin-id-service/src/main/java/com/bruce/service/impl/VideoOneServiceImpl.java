package com.bruce.service.impl;

import com.bruce.dao.VideoOneDao;
import com.bruce.service.VideoOneService;
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
    public List<Map> getVideoInfo(int page, int size,String videoType) {
        int limit = size;
        int offset = (page - 1) * size;

        List<Map> one =videoOneDao.queryVideoInfo(limit, offset,videoType);
        return one ;
    }

//    @Override
//    public void insertlianjie(String lianjie) {
//        videoOneDao.insertLianjie(lianjie);
//    }
}

