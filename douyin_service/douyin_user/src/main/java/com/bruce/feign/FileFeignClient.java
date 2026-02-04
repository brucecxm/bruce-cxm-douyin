package com.bruce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传Feign客户端
 * 用于调用文件上传服务
 */
@FeignClient(name = "DouyinCommonService", path = "/file")
public interface FileFeignClient {

    /**
     * 上传图片文件
     * @param file 图片文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    @PostMapping(value = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false, defaultValue = "images") String prefix
    );

    /**
     * 上传视频文件
     * @param file 视频文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    @PostMapping(value = "/upload/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadVideo(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false, defaultValue = "videos") String prefix
    );

    /**
     * 通用文件上传
     * @param file 文件
     * @param prefix 文件前缀，用于分类存储
     * @param fileType 文件类型(image/video/html/other)
     * @return 上传结果，包含文件URL
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadAnyFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false, defaultValue = "files") String prefix,
            @RequestParam(value = "fileType", required = false, defaultValue = "other") String fileType
    );
}
