package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserRoleDao;
import com.bruce.entity.UserRole;
import com.bruce.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (UserRole)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 20:01:02
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        this.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        for (Long roleId : roleIds) {
            this.save(new UserRole(userId, roleId));
        }
    }
}

