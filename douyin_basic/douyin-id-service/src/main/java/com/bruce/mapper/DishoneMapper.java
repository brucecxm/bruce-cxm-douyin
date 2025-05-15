package com.bruce.mapper;


import com.bruce.dto.DishDto;
import com.bruce.dto.dishoneDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DishoneMapper {
     List<dishoneDto> getdishone();
     DishDto searchdish(String name);
     List<DishDto> liansearchdish(String name);
}
