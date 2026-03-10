package com.bruce.service.strategy.impl;

import com.bruce.service.strategy.VideoStrategy;
import com.bruce.vo.VideoVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频策略2（对应videoType=2）
 */
@Component("videoStrategy2")
public class VideoStrategy2 implements VideoStrategy {

    @Override
    public List<VideoVO> getVideos(int page, int size) {
        // 示例：返回空列表，后续根据具体需求实现
        return new ArrayList<>();
    }
}
