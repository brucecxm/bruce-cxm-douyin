package com.bruce.service;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bruketName;

    //封装后的上传文件的方法
    public  String uploadFile(MultipartFile file) throws IOException, ServerException,InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String filename=file.getOriginalFilename();
        InputStream inputStream=file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bruketName)
                        .object(filename)
                        .stream(inputStream,file.getSize(),-1)
                        .contentType(file.getContentType()).build()
        );


        return filename;
    }

}
