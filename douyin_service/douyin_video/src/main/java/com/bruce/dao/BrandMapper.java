package com.bruce.dao;

import com.bruce.video.entity.Brand;

import java.util.List;

public interface BrandMapper {

    /**
     * 查询所有
     */
    List<Brand> selectAll();
}