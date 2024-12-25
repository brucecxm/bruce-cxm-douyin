package com.bruce;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("DouyinMusicService")
public interface MusicClient {
    @GetMapping("/musicfeign/avatar")
    public String avatar(@RequestParam int musicId);


}
