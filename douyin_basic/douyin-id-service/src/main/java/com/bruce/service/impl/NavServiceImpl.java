package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.NavDao;
import com.bruce.entity.Nav;
import com.bruce.service.NavService;
import org.springframework.stereotype.Service;

/**
 * (Nav)表服务实现类
 *
 * @author makejava
 * @since 2024-11-06 00:40:09
 */
@Service("navService")
public class NavServiceImpl extends ServiceImpl<NavDao, Nav> implements NavService {

}

