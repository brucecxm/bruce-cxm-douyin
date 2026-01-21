package com.bruce.mapper;

import com.bruce.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicMapper {
    //获取所有话题列表
    @Select("select * from topic order by is_hot desc, create_time desc")
    List<Topic> findAll();
    
    //根据ID查询话题
    @Select("select * from topic where id=#{id}")
    Topic findById(Long id);
    
    //获取热门话题
    @Select("select * from topic where is_hot=1 order by create_time desc")
    List<Topic> findHotTopics();
}