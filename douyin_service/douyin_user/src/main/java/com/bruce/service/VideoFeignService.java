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
                    return (List<Video>) data;
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
        // 实际项目中，这里需要使用反射或其他方式从queryWrapper中提取参数值
        // 这里简化处理，返回null
        // 实际实现中，可能需要使用MyBatis Plus的内部API或自定义解析逻辑
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
                }
            }
        }
        return new Page<>();
    }
}
