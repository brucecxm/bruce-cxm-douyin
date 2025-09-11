package com.bruce.service.impl;


import com.bruce.dto.categoryDto;
import com.bruce.mapper.categoryone;
import com.bruce.service.CategoryoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryoneServiceimpl implements CategoryoneService {
    @Autowired
    private categoryone categoryonet;


    @Override
    public List<categoryDto> getCategoryoneList() {
        return categoryonet.getcategoryimg();
    }
}
