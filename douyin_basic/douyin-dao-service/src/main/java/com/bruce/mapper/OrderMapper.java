package com.bruce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}