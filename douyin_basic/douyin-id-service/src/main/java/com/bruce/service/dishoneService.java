package com.bruce.service;

import com.bruce.dto.DishDto;
import com.bruce.dto.dishoneDto;

import java.util.List;

public interface dishoneService {
    List<dishoneDto> getdishone();
     DishDto searchdish(String name);
     List<DishDto> liansearchdish(String name);

}
