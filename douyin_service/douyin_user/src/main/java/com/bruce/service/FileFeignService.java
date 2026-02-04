package com.bruce.service;

import com.bruce.feign.FileFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传Feign服务
 * 用于调用文件上传微服务
 */
@Service
public class FileFeignService {

    @Autowired
    private FileFeignClient fileFeignClient;

    /**
     * 上传图片文件
     * @param file 图片文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    public Map<String, Object> uploadImage(MultipartFile file, String prefix) {
        return fileFeignClient.uploadImage(file, prefix);
    }

    /**
     * 上传视频文件
     * @param file 视频文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    public Map<String, Object> uploadVideo(MultipartFile file, String prefix) {
        return fileFeignClient.uploadVideo(file, prefix);
    }

    /**
     * 通用文件上传
     * @param file 文件
     * @param prefix 文件前缀，用于分类存储
     * @param fileType 文件类型(image/video/html/other)
     * @return 上传结果，包含文件URL
     */
    public Map<String, Object> uploadAnyFile(MultipartFile file, String prefix, String fileType) {
        return fileFeignClient.uploadAnyFile(file, prefix, fileType);
    }
}
