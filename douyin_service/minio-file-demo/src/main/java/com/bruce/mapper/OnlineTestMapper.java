package com.bruce.mapper;


import com.bruce.entity.OnlineTest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OnlineTestMapper {
    List<OnlineTest> getOnlineTestData();
}
