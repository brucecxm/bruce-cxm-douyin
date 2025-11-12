package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.InterfaceLogDao;
import com.bruce.entity.InterfaceLog;
import com.bruce.service.InterfaceLogService;
import org.springframework.stereotype.Service;

/**
 * 接口访问日志表（记录UserController所有接口调用信息）(InterfaceLog)表服务实现类
 *
 * @author makejava
 * @since 2025-11-12 09:24:34
 */
@Service("interfaceLogService")
public class InterfaceLogServiceImpl extends ServiceImpl<InterfaceLogDao, InterfaceLog> implements InterfaceLogService {

}

