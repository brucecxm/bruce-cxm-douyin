package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.LocationDao;
import com.bruce.entity.Location;
import com.bruce.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Location)表服务实现类
 *
 * @author bruce
 * @since 2026-01-21 15:00:00
 */
@Service("locationService")
public class LocationServiceImpl extends ServiceImpl<LocationDao, Location> implements LocationService {

    @Override
    public List<Location> getAllLocations() {
        return this.list();
    }

    @Override
    public Location getLocationById(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Location> getLocationsByCity(String city) {
        return this.lambdaQuery().eq(Location::getCity, city).list();
    }
}