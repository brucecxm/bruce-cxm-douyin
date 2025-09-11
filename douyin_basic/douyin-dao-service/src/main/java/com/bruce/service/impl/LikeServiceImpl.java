package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.LikeDao;
import com.bruce.entity.Like;
import com.bruce.service.LikeService;
import org.springframework.stereotype.Service;

/**
 * (Like)表服务实现类
 *
 * @author makejava
 * @since 2025-06-04 15:26:50
 */
@Service("likeService")
public class LikeServiceImpl extends ServiceImpl<LikeDao, Like> implements LikeService {

}

