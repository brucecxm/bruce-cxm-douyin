package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.RolePermissionDao;
import com.bruce.entity.RolePermission;
import com.bruce.service.RolePermissionService;
import org.springframework.stereotype.Service;

/**
 * (RolePermission)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 20:00:38
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {

}

