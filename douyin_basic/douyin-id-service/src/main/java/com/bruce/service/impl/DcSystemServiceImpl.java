package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.DcSystemDao;
import com.bruce.entity.DcSystem;
import com.bruce.service.DcSystemService;
import org.springframework.stereotype.Service;

/**
 * (DcSystem)表服务实现类
 *
 * @author makejava
 * @since 2025-02-20 18:54:15
 */
@Service("dcSystemService")
public class DcSystemServiceImpl extends ServiceImpl<DcSystemDao, DcSystem> implements DcSystemService {

}

