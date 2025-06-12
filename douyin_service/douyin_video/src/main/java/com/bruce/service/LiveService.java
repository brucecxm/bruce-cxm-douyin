package com.bruce.service;


import com.bruce.entity.Result;
import com.bruce.entitys.Live;

import java.util.List;

public interface LiveService {
    Result startLive(Live live);
    Result endLive(Long liveId);
    Result<List<Live>> getLiveList();
    Result<Live> getLiveDetail(Long liveId);
    Result<String> getAgoraToken(String channelName, String userId);
}
