package com.bruce.service.Impl;

import com.bruce.entity.Result;
import com.bruce.entitys.Live;
import com.bruce.mapper.LiveMapper;
import com.bruce.service.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LiveServiceImpl implements LiveService {

    @Autowired
    private LiveMapper liveMapper;

    @Value("${agora.appId}")
    private String appId;

    @Value("${agora.appCertificate}")
    private String appCertificate;

    @Override
    public Result startLive(Live live) {
        live.setStatus(1); // 1表示直播中
        live.setStartTime(new Date());
        liveMapper.insert(live);
        return Result.success("开始直播成功");
    }

    @Override
    public Result endLive(Long liveId) {
        Live live = liveMapper.selectById(liveId);
        if (live == null) {
            return Result.error("直播不存在");
        }
        live.setStatus(0); // 0表示已结束
        live.setEndTime(new Date());
        liveMapper.updateById(live);
        return Result.success("结束直播成功");
    }

    @Override
    public Result<List<Live>> getLiveList() {
        List<Live> liveList = liveMapper.selectListByStatus(1); // 获取所有直播中的
        return Result.success(liveList);
    }

    @Override
    public Result<Live> getLiveDetail(Long liveId) {
        Live live = liveMapper.selectById(liveId);

        return Result.success(live);
    }

    @Override
    public Result<String> getAgoraToken(String channelName, String userId) {
        return null;
    }
//
//    @Override
//    public Result<String> getAgoraToken(String channelName, String userId) {
//        try {
//            int expirationTimeInSeconds = 3600;
//            int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
//
//            RtcTokenBuilder token = new RtcTokenBuilder();
//            Role role = Role.ROLE_PUBLISHER; // 主播角色
//
//            String result = token.buildTokenWithUid(appId, appCertificate, channelName,
//                    Integer.parseInt(userId), role, timestamp);
//
//            return Result.success(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("生成Token失败");
//        }
//    }
}
