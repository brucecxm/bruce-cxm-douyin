package com.bruce.service;


import java.util.List;
import java.util.Map;

/**
 * (Video)表服务接口
 *
 * @author makejava
 * @since 2024-06-25 13:03:25
 */
public interface VideoOneService {
 List<Map> getVideoInfo(int page, int size,String videoType);
}

