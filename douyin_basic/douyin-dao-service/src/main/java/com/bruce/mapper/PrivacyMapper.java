package com.bruce.mapper;

import com.bruce.entity.Privacy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrivacyMapper {
    //获取所有权限设置列表
    @Select("select * from privacy order by create_time desc")
    List<Privacy> findAll();
    
    //根据ID查询权限设置
    @Select("select * from privacy where id=#{id}")
    Privacy findById(Long id);
    
    //根据名称查询权限设置
    @Select("select * from privacy where name=#{name}")
    Privacy findByName(String name);
}