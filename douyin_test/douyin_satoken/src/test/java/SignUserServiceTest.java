package com.ceam.aop.service;

import cn.dev33.satoken.stp.StpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignUserServiceTest {


    @Test
    public void dod() {
        // 会话登录：参数填写要登录的账号id，建议的数据类型：long | int | String， 不可以传入复杂类型，如：User、Admin 等等
        StpUtil.login(10);

        System.out.println(        StpUtil.getPermissionList()
);
        System.out.println(       StpUtil.getRoleList());
        System.out.println(        StpUtil.hasRole("super-adminsss"));
        // 判断：当前账号是否拥有指定角色, 返回 true 或 false

// 校验：当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
        StpUtil.checkRole("super-admissssn");
    }
}