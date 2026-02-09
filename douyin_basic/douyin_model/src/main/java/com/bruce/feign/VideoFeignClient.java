package com.bruce.feign;

import com.bruce.entity.Video;
import com.bruce.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "DouyinVideoService", path = "/video")
public interface VideoFeignClient {

    @GetMapping("/getById")
    Result<Video> getVideoById(@RequestParam("id") Long id);

    @GetMapping("/list")
    Result<?> getVideoList();

    @GetMapping("/listByUserId")
    Result<?> listByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/pageByUserId")
    Result<?> pageByUserId(@RequestParam("userId") Long userId, @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize);
}
