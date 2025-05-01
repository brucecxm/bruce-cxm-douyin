package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.SaltDao;
import com.bruce.entity.Salt;
import com.bruce.service.SaltService;
import org.springframework.stereotype.Service;

/**
 * (Salt)表服务实现类
 *
 * @author makejava
 * @since 2025-02-20 15:01:08
 */
@Service("saltService")
public class SaltServiceImpl extends ServiceImpl<SaltDao, Salt> implements SaltService {

}

