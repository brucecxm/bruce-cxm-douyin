package com.bruce;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Video;
import com.bruce.mapper.SysUserMapper;
import com.bruce.pojo.SysUser;
import com.bruce.service.VideoFeignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private VideoFeignService videoFeignService;

    @Test
    void contextLoads() {
        Page<Video> pageQuery = new Page<>(1, 10);
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getUserId, 480971991428129L); // 必须加 L

        IPage<Video> videoPage = videoFeignService.page(pageQuery, wrapper);
        System.out.println("结果数量：" + videoPage.getRecords().size());
    }
}
