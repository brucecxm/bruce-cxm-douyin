package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FavorDao;
import com.bruce.entity.Favor;
import com.bruce.service.FavorService;
import org.springframework.stereotype.Service;

/**
 * (Favor)表服务实现类
 *
 * @author makejava
 * @since 2025-06-10 10:37:16
 */
@Service("favorService")
public class FavorServiceImpl extends ServiceImpl<FavorDao, Favor> implements FavorService {

}

