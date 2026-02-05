package com.bruce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.Video;
import com.bruce.feign.VideoFeignClient;
import com.bruce.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VideoFeignService {

    @Autowired
    private VideoFeignClient videoFeignClient;

    /**
     * 通过Feign调用douyin_video微服务，根据ID获取视频
     *
     * @param id 视频ID
     * @return 视频信息
     */
    public Object getVideoById(Long id) {
        return videoFeignClient.getVideoById(id);
    }

    /**
     * 通过Feign调用douyin_video微服务，获取视频列表
     *
     * @return 视频列表
     */
    public Object getVideoList() {
        return videoFeignClient.getVideoList();
    }

    /**
     * 通过Feign调用douyin_video微服务，根据用户ID获取视频列表
     *
     * @param queryWrapper 查询条件
     * @return 视频列表
     */
    public List<Video> list(LambdaQueryWrapper<Video> queryWrapper) {
        // 注意：由于Feign无法直接传递LambdaQueryWrapper，这里我们需要从查询条件中提取参数
        // 实际实现中，应该根据查询条件构建正确的请求
        // 这里简化处理，假设查询条件中包含userId
        // 从查询条件中提取userId
        Long userId = null;
        try {
            // 解析LambdaQueryWrapper获取userId参数
            // 这里简化处理，实际项目中可能需要更复杂的解析逻辑
            // 假设查询条件中只包含userId的等值查询
            Object paramValue = getQueryParamValue(queryWrapper, Video::getUserId);
            if (paramValue != null) {
                userId = (Long) paramValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId != null) {
            // 调用videoFeignClient.listByUserId(userId)
            Object result = videoFeignClient.listByUserId(userId);
            // 处理返回结果
            if (result instanceof Result) {
                Result<?> resultObj = (Result<?>) result;
                Object data = resultObj.getData();
                if (data instanceof List) {
                    // 尝试将List中的对象转换为Video类型
                    List<?> dataList = (List<?>) data;
                    List<Video> videoList = new ArrayList<>();
                    for (Object item : dataList) {
                        if (item instanceof Video) {
                            videoList.add((Video) item);
                        } else if (item instanceof Map) {
                            // 如果是Map类型，尝试转换为Video对象
                            Map<?, ?> map = (Map<?, ?>) item;
                            Video video = new Video();
                            video.setVideoId(map.containsKey("videoId") ? (Long) map.get("videoId") : null);
                            video.setUserId(map.containsKey("userId") ? (Long) map.get("userId") : null);
                            video.setMusicId(map.containsKey("musicId") ? (Long) map.get("musicId") : null);
                            video.setUrl(map.containsKey("url") ? (String) map.get("url") : null);
                            video.setContent(map.containsKey("content") ? (String) map.get("content") : null);
                            video.setTitle(map.containsKey("title") ? (String) map.get("title") : null);
                            video.setCover(map.containsKey("cover") ? (String) map.get("cover") : null);
                            video.setType(map.containsKey("type") ? (Integer) map.get("type") : null);
                            video.setStatus(map.containsKey("status") ? (Integer) map.get("status") : null);
                            videoList.add(video);
                        }
                    }
                    return videoList;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 从LambdaQueryWrapper中提取查询参数值
     *
     * @param queryWrapper 查询条件
     * @param column       列名
     * @param <T>          实体类型
     * @param <R>          列类型
     * @return 参数值
     */
    private <T, R> Object getQueryParamValue(LambdaQueryWrapper<T> queryWrapper,
            java.util.function.Function<T, R> column) {
        try {
            // 使用反射获取LambdaQueryWrapper中的query对象
            java.lang.reflect.Field field = queryWrapper.getClass().getDeclaredField("entity");
            field.setAccessible(true);
            T entity = (T) field.get(queryWrapper);
            if (entity != null) {
                // 调用column函数获取属性值
                return column.apply(entity);
            }
        } catch (Exception e) {
            // 如果通过entity获取失败，尝试从查询条件中解析
            try {
                // 获取queryWrapper中的查询条件
                java.lang.reflect.Field field = queryWrapper.getClass().getDeclaredField("query");
                field.setAccessible(true);
                Object query = field.get(queryWrapper);

                // 获取查询条件中的where条件
                java.lang.reflect.Field whereField = query.getClass().getDeclaredField("where");
                whereField.setAccessible(true);
                Object where = whereField.get(query);

                // 这里简化处理，实际项目中可能需要更复杂的解析逻辑
                // 假设where是一个List，包含了查询条件
                if (where instanceof java.util.List) {
                    java.util.List<?> list = (java.util.List<?>) where;
                    for (Object obj : list) {
                        // 尝试从条件中提取userId值
                        // 这里简化处理，实际项目中可能需要更复杂的解析
                        if (obj != null) {
                            java.lang.reflect.Field columnField = obj.getClass().getDeclaredField("column");
                            columnField.setAccessible(true);
                            Object columnObj = columnField.get(obj);
                            if (columnObj != null && columnObj.toString().contains("userId")) {
                                java.lang.reflect.Field valField = obj.getClass().getDeclaredField("val");
                                valField.setAccessible(true);
                                return valField.get(obj);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过Feign调用douyin_video微服务，根据用户ID分页获取视频列表
     *
     * @param page         分页对象
     * @param queryWrapper 查询条件
     * @return 分页视频列表
     */
    public IPage<Video> page(Page<Video> page, LambdaQueryWrapper<Video> queryWrapper) {
        // 注意：由于Feign无法直接传递LambdaQueryWrapper，这里我们需要从查询条件中提取参数
        // 实际实现中，应该根据查询条件构建正确的请求
        // 这里简化处理，假设查询条件中包含userId
        // 从查询条件中提取userId
        Long userId = null;
        try {
            // 解析LambdaQueryWrapper获取userId参数
            // 这里简化处理，实际项目中可能需要更复杂的解析逻辑
            // 假设查询条件中只包含userId的等值查询
            Object paramValue = getQueryParamValue(queryWrapper, Video::getUserId);
            if (paramValue != null) {
                userId = (Long) paramValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userId != null) {
            // 调用videoFeignClient.pageByUserId(userId, page.getCurrent(), page.getSize())
            Object result = videoFeignClient.pageByUserId(userId, page.getCurrent(), page.getSize());
            // 处理返回结果
            if (result instanceof Result) {
                Result<?> resultObj = (Result<?>) result;
                Object data = resultObj.getData();
                if (data instanceof IPage) {
                    return (IPage<Video>) data;
                } else if (data instanceof Map) {
                    // 如果是Map类型，尝试转换为Page<Video>对象
                    Map<?, ?> map = (Map<?, ?>) data;
                    Page<Video> resultPage = new Page<>();
                    resultPage.setCurrent(
                            map.containsKey("current") ? Long.parseLong(map.get("current").toString()) : 1L);
                    resultPage.setSize(map.containsKey("size") ? Long.parseLong(map.get("size").toString()) : 10L);
                    resultPage.setTotal(map.containsKey("total") ? Long.parseLong(map.get("total").toString()) : 0L);

                    // 处理records
                    if (map.containsKey("records") && map.get("records") instanceof List) {
                        List<?> recordsList = (List<?>) map.get("records");
                        List<Video> videoList = new ArrayList<>();
                        for (Object item : recordsList) {
                            if (item instanceof Video) {
                                videoList.add((Video) item);
                            } else if (item instanceof Map) {
                                // 如果是Map类型，尝试转换为Video对象
                                Map<?, ?> itemMap = (Map<?, ?>) item;
                                Video video = new Video();
                                video.setVideoId(itemMap.containsKey("videoId") ? (Long) itemMap.get("videoId") : null);
                                video.setUserId(itemMap.containsKey("userId") ? (Long) itemMap.get("userId") : null);
                                video.setMusicId(itemMap.containsKey("musicId") ? (Long) itemMap.get("musicId") : null);
                                video.setUrl(itemMap.containsKey("url") ? (String) itemMap.get("url") : null);
                                video.setContent(
                                        itemMap.containsKey("content") ? (String) itemMap.get("content") : null);
                                video.setTitle(itemMap.containsKey("title") ? (String) itemMap.get("title") : null);
                                video.setCover(itemMap.containsKey("cover") ? (String) itemMap.get("cover") : null);
                                video.setType(itemMap.containsKey("type") ? (Integer) itemMap.get("type") : null);
                                video.setStatus(itemMap.containsKey("status") ? (Integer) itemMap.get("status") : null);
                                videoList.add(video);
                            }
                        }
                        resultPage.setRecords(videoList);
                    }

                    return resultPage;
                }
            }
        }
        return new Page<>();
    }
}
