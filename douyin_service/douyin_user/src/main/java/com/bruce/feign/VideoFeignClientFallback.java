package com.bruce.feign;

import com.bruce.entity.Video;
import com.bruce.pojo.Result;
import org.springframework.stereotype.Component;

/**
 * VideoFeignClient的降级实现类
 */
@Component
public class VideoFeignClientFallback implements VideoFeignClient {

    @Override
    public Result<Video> getVideoById(Long id) {
        return Result.error("获取视频失败，服务暂时不可用");
    }

    @Override
    public Result<?> getVideoList() {
        return Result.error("获取视频列表失败，服务暂时不可用");
    }

    @Override
    public Result<?> listByUserId(Long userId) {
        return Result.error("获取用户视频列表失败，服务暂时不可用");
    }

    @Override
    public Result<?> pageByUserId(Long userId, Long page, Long pageSize) {
        // ANSI 红色代码：\033[91m   重置颜色：\033[0m
        String red = "\033[91m";
        String reset = "\033[0m";
        String line = "============================================";

        // 循环打印 100 行 红色分隔线
        for (int i = 0; i < 100; i++) {
            System.out.println(red + line + reset);
        }
        return Result.error("分页获取用户视频失败，服务暂时不可用");
    }
}
