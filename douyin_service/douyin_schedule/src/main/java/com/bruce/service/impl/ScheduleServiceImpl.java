package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.ScheduleDao;
import com.bruce.entity.Schedule;
import com.bruce.service.ScheduleService;
import org.springframework.stereotype.Service;

/**
 * (Schedule)表服务实现类
 *
 * @author makejava
 * @since 2025-01-22 00:37:54
 */
@Service("scheduleService")
public class ScheduleServiceImpl extends ServiceImpl<ScheduleDao, Schedule> implements ScheduleService {

}

