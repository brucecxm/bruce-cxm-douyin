package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Topic;

import java.util.List;

/**
 * (Topic)表服务接口
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
public interface TopicService extends IService<Topic> {
    
    /**
     * 获取所有话题列表
     * @return 话题列表
     */
    List<Topic> getAllTopics();
    
    /**
     * 获取热门话题列表
     * @return 热门话题列表
     */
    List<Topic> getHotTopics();
    
    /**
     * 根据ID获取话题
     * @param id 话题ID
     * @return 话题对象
     */
    Topic getTopicById(Long id);
}