package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
