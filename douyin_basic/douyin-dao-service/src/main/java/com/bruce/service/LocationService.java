package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.Location;

import java.util.List;

/**
 * (Location)表服务接口
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
public interface LocationService extends IService<Location> {
    
    /**
     * 获取所有位置列表
     * @return 位置列表
     */
    List<Location> getAllLocations();
    
    /**
     * 根据ID获取位置
     * @param id 位置ID
     * @return 位置对象
     */
    Location getLocationById(Long id);
    
    /**
     * 根据城市获取位置列表
     * @param city 城市名称
     * @return 位置列表
     */
    List<Location> getLocationsByCity(String city);
}