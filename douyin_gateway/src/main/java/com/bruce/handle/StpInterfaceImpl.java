package com.bruce.handle;

import cn.dev33.satoken.stp.StpInterface;
import com.bruce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private AuthService authService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 使用AuthService获取用户权限码列表
        if (loginId == null) {
            return null;
        }
        Long userId = Long.parseLong(loginId.toString());
        return authService.getUserPermissionCodes(userId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 使用AuthService获取用户角色码列表
        if (loginId == null) {
            return null;
        }
        Long userId = Long.parseLong(loginId.toString());
        return authService.getUserRoleCodes(userId);
    }

}
