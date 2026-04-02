package com.bruce.service.strategy.impl;

import com.bruce.service.strategy.VideoStrategy;
import com.bruce.vo.VideoVO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频策略4（对应videoType=4）
 */
@Component("videoStrategy4")
public class VideoStrategy4 implements VideoStrategy {
    // 加上这个！
//    @PostConstruct
//    public void init() {
//        System.out.println("========== VideoStrategy4 初始化成功！ ==========");
//    }
    @Override
    public List<VideoVO> getVideos(int page, int size) {
        // 示例：返回空列表，后续根据具体需求实现
        return new ArrayList<>();
    }
}
