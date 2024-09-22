package com.bruce;

import com.bruce.mapper.SysUserMapper;
import com.bruce.pojo.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("lisi");
        sysUser.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.insert(sysUser);
    }
}
