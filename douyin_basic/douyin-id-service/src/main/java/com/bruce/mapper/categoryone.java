package com.bruce.mapper;


import com.bruce.dto.categoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface categoryone {

    List<categoryDto> getcategoryimg();


}
