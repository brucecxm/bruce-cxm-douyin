package com.bruce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.PermissionDao;
import com.bruce.entity.Permission;
import com.bruce.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * (Permission)表服务实现类
 *
 * @author makejava
 * @since 2025-05-23 17:34:08
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

}

