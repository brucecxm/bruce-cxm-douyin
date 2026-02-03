package com.bruce.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "DouyinVideoService", path = "/video")
public interface VideoFeignClient {

    @GetMapping("/getById")
    Object getVideoById(@RequestParam("id") Long id);

    @GetMapping("/list")
    Object getVideoList();

    @GetMapping("/listByUserId")
    Object listByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/pageByUserId")
    Object pageByUserId(@RequestParam("userId") Long userId, @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize);
}
