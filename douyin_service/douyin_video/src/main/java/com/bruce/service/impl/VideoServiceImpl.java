package com.bruce.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.VideoDao;
import com.bruce.dict.Constant;
import com.bruce.entity.*;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.*;
import com.bruce.service.strategy.VideoStrategy;
import com.bruce.service.strategy.VideoStrategyFactory;
import com.bruce.vo.VideoVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Video)表服务实现类
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private MessageQueueService messageQueueService;

    @Autowired
    private IdService idService;

    @Autowired
    private FileStorageService fileStorageService;

    @Resource
    private UserService userService;

    @Resource
    private MusicService musicService;

    @Resource
    private LikeService likeService;

    @Resource
    private FavorService favorService;
    @Resource
    private CommentService commentService;

    @Resource
    private VideoStrategyFactory videoStrategyFactory;

    @Override
    public List<Map<String, Object>> getVideoPage(int page,
            int size,
            String videoType) {
        // 根据videoType获取对应的策略
        VideoStrategy strategy = videoStrategyFactory.getStrategy(videoType);
        // 调用策略的getVideos方法获取视频列表
        return strategy.getVideos(page, size);
    }

}
