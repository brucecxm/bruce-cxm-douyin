package com.bruce.service;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private MinioClient minioClient;

    private final String BUCKET_NAME = "mybucket"; // 替换为你的桶名

    public void uploadFile(String objectName, InputStream inputStream, long size) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .build());
    }

    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                .build());
    }
}
