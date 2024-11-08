package com.one.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.dao.NavDao;
import com.one.entity.Nav;
import com.one.service.NavService;
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

