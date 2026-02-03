package com.bruce.service.strategy.impl;

import com.bruce.service.strategy.VideoStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频策略4（对应videoType=4）
 */
@Component("videoStrategy4")
public class VideoStrategy4 implements VideoStrategy {

    @Override
    public List<Map<String, Object>> getVideos(int page, int size) {
        // TODO: 实现videoType=4的视频获取逻辑
        // 示例：返回空列表，后续根据具体需求实现
        return new ArrayList<>();
    }
}
