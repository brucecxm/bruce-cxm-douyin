package com.bruce.service.impl;

import com.bruce.dto.DishDto;
import com.bruce.dto.dishoneDto;
import com.bruce.mapper.DishoneMapper;
import com.bruce.service.dishoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class dishoneServiceimpl implements dishoneService {


    @Autowired
    private DishoneMapper dishoneMapper;


    @Override
    public List<dishoneDto> getdishone() {
        List<dishoneDto> one=  dishoneMapper.getdishone();
        one.addAll(dishoneMapper.getdishone());
        return one;
    }

    @Override
    public DishDto searchdish(String name) {
     DishDto dishDto=   dishoneMapper.searchdish(name);
        return dishDto;
    }

    @Override
    public List<DishDto> liansearchdish(String name) {
        List<DishDto> dishDto=   dishoneMapper.liansearchdish(name);
        return dishDto;
    }
}
