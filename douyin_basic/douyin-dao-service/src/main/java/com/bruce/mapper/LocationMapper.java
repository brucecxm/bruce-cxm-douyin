package com.bruce.mapper;

import com.bruce.entity.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LocationMapper {
    //获取所有位置列表
    @Select("select * from location order by create_time desc")
    List<Location> findAll();
    
    //根据ID查询位置
    @Select("select * from location where id=#{id}")
    Location findById(Long id);
    
    //根据城市查询位置
    @Select("select * from location where city=#{city} order by create_time desc")
    List<Location> findByCity(String city);
}