package com.example.apiserver.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class MinioService {
    private static final Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.readPath}")
    private String readPath;

    /**
     * 上传图片到MinIO
     */
    public String uploadImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("只能上传图片文件");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String filename = "product/" + dateDir + "/" + UUID.randomUUID() + ext;

            // 上传文件到MinIO
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(filename)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(contentType)
                        .build();
                minioClient.putObject(args);
            }

            // 返回可访问的图片URL
            return readPath + "/" + bucket + "/" + filename;
        } catch (Exception e) {
            logger.error("上传图片失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 批量上传图片到MinIO
     */
    public String[] uploadImages(MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String[] imageUrls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            imageUrls[i] = uploadImage(files[i]);
        }

        return imageUrls;
    }
}