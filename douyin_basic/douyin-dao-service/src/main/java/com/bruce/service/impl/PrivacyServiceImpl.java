package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.PrivacyDao;
import com.bruce.entity.Privacy;
import com.bruce.service.PrivacyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Privacy)表服务实现类
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
@Service("privacyService")
public class PrivacyServiceImpl extends ServiceImpl<PrivacyDao, Privacy> implements PrivacyService {

    @Override
    public List<Privacy> getAllPrivacies() {
        return this.list();
    }

    @Override
    public Privacy getPrivacyById(Long id) {
        return this.getById(id);
    }

    @Override
    public Privacy getPrivacyByName(String name) {
        return this.lambdaQuery().eq(Privacy::getName, name).one();
    }
}