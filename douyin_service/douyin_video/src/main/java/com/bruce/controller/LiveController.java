package com.bruce.controller;

import com.bruce.entity.Result;
import com.bruce.entitys.Live;
import com.bruce.service.LiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/live")
@Api(tags = "直播管理")
public class LiveController {

    @Autowired
    private LiveService liveService;

    @PostMapping("/start")
    @ApiOperation("开始直播")
    public Result startLive(@RequestBody Live live) {
        return liveService.startLive(live);
    }

    @PostMapping("/end")
    @ApiOperation("结束直播")
    public Result endLive(@RequestParam Long liveId) {
        return liveService.endLive(liveId);
    }

    @GetMapping("/list")
    @ApiOperation("获取直播列表")
    public Result<List<Live>> getLiveList() {
        return liveService.getLiveList();
    }

    @GetMapping("/{liveId}")
    @ApiOperation("获取直播详情")
    public Result<Live> getLiveDetail(@PathVariable Long liveId) {
        return liveService.getLiveDetail(liveId);
    }

    @GetMapping("/token")
    @ApiOperation("获取Agora Token")
    public Result<String> getAgoraToken(@RequestParam String channelName, @RequestParam String userId) {
        return liveService.getAgoraToken(channelName, userId);
    }
}
