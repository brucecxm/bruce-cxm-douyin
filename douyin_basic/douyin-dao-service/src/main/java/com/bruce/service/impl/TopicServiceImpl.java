package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.TopicDao;
import com.bruce.entity.Topic;
import com.bruce.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Topic)表服务实现类
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
@Service("topicService")
public class TopicServiceImpl extends ServiceImpl<TopicDao, Topic> implements TopicService {

    @Override
    public List<Topic> getAllTopics() {
        return this.list();
    }

    @Override
    public List<Topic> getHotTopics() {
        return this.lambdaQuery().eq(Topic::getIsHot, 1).list();
    }

    @Override
    public Topic getTopicById(Long id) {
        return this.getById(id);
    }
}