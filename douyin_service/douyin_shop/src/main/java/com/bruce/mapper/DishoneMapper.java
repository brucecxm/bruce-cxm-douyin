package com.bruce.mapper;


import com.bruce.dto.dishoneDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DishoneMapper {
     List<dishoneDto> getdishone();
}
