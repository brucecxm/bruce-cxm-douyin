package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Privacy;

import java.util.List;

/**
 * (Privacy)表服务接口
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
public interface PrivacyService extends IService<Privacy> {
    
    /**
     * 获取所有权限设置列表
     * @return 权限设置列表
     */
    List<Privacy> getAllPrivacies();
    
    /**
     * 根据ID获取权限设置
     * @param id 权限设置ID
     * @return 权限设置对象
     */
    Privacy getPrivacyById(Long id);
    
    /**
     * 根据名称获取权限设置
     * @param name 权限设置名称
     * @return 权限设置对象
     */
    Privacy getPrivacyByName(String name);
}