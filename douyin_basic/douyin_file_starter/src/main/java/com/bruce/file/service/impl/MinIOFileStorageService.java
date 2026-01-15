package com.bruce.file.service.impl;


import com.bruce.file.config.MinIOConfig;
import com.bruce.file.config.MinIOConfigProperties;
import com.bruce.file.service.FileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
public class MinIOFileStorageService implements FileStorageService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    private final static String separator = "/";

    /**
     * @param dirPath
     * @param filename  yyyy/mm/dd/file.jpg
     * @return
     */
    public String builderFilePath(String dirPath,String filename) {
        StringBuilder stringBuilder = new StringBuilder(50);
        if(!StringUtils.isEmpty(dirPath)){
            stringBuilder.append(dirPath).append(separator);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(separator);
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }


    /**
     * 上传视频文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 视频流
     * @return 视频在 MinIO 上的完整访问路径
     */
    @Override
    public String uploadVideoFile(String prefix, String filename, InputStream inputStream) {
        // 构建最终存储路径，例如 "prefix/yyyy/MM/dd/filename.mp4"
        String filePath = builderFilePath(prefix, filename);
        try {
            // 准备上传参数：bucket、object 名称、内容类型、流
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minIOConfigProperties.getBucket())
                    .object(filePath)
                    // 根据文件扩展名动态设置内容类型
                    .contentType(detectContentType(filename))
                    // 传入流长度为 -1，并由 MinIO SDK 自动分片上传
                    .stream(inputStream, -1, 5 * 1024 * 1024)
                    .build();

            // 执行上传
            minioClient.putObject(putObjectArgs);

            // 构造可访问 URL：baseReadPath/bucket/filePath
            StringBuilder url = new StringBuilder(minIOConfigProperties.getReadPath());
            url.append(separator).append(minIOConfigProperties.getBucket())
                    .append(separator).append(filePath);

            return url.toString();
        } catch (Exception ex) {
            log.error("minio put video error.", ex);
            throw new RuntimeException("上传视频失败", ex);
        }
    }
    
    /**
     * 根据文件名检测文件类型
     */
    private String detectContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            // 图片类型
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            // 视频类型
            case "mp4":
                return "video/mp4";
            case "mov":
                return "video/quicktime";
            case "avi":
                return "video/x-msvideo";
            case "wmv":
                return "video/x-ms-wmv";
            case "flv":
                return "video/x-flv";
            case "mkv":
                return "video/x-matroska";
            // 默认类型
            default:
                return "application/octet-stream";
        }
    }


    /**
     *  上传图片文件
     * @param prefix  文件前缀
     * @param filename  文件名
     * @param inputStream 文件流
     * @return  文件全路径
     */
    @Override
    public String uploadImgFile(String prefix, String filename,InputStream inputStream) {
        String filePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("image/jpg")
                    .bucket(minIOConfigProperties.getBucket()).stream(inputStream,inputStream.available(),-1)
                    .build();
            minioClient.putObject(putObjectArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator+minIOConfigProperties.getBucket());
            urlPath.append(separator);
            urlPath.append(filePath);
            return urlPath.toString();
        }catch (Exception ex){
            log.error("minio put file error.",ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     *  上传html文件
     * @param prefix  文件前缀
     * @param filename   文件名
     * @param inputStream  文件流
     * @return  文件全路径
     */
    @Override
    public String uploadHtmlFile(String prefix, String filename,InputStream inputStream) {
        String filePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("text/html")
                    .bucket(minIOConfigProperties.getBucket()).stream(inputStream,inputStream.available(),-1)
                    .build();
            minioClient.putObject(putObjectArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator+minIOConfigProperties.getBucket());
            urlPath.append(separator);
            urlPath.append(filePath);
            return urlPath.toString();
        }catch (Exception ex){
            log.error("minio put file error.",ex);
            ex.printStackTrace();
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 删除文件
     * @param pathUrl  文件全路径
     */
    @Override
    public void delete(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint()+"/","");
        int index = key.indexOf(separator);
        String bucket = key.substring(0,index);
        String filePath = key.substring(index+1);
        // 删除Objects
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).object(filePath).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("minio remove file error.  pathUrl:{}",pathUrl);
            e.printStackTrace();
        }
    }


    /**
     * 下载文件
     * @param pathUrl  文件全路径
     * @return  文件流
     *
     */
    @Override
    public byte[] downLoadFile(String pathUrl)  {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint()+"/","");
        int index = key.indexOf(separator);
        String bucket = key.substring(0,index);
        String filePath = key.substring(index+1);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minIOConfigProperties.getBucket()).object(filePath).build());
        } catch (Exception e) {
            log.error("minio down file error.  pathUrl:{}",pathUrl);
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while (true) {
            try {
                if (!((rc = inputStream.read(buff, 0, 100)) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 通用上传文件到 MinIO 指定前缀下，可自动根据文件名推断 contentType
     *
     * @param prefix       MinIO 内 testfile 等前缀
     * @param filename     文件名
     * @param inputStream  文件流
     * @param contentType  可为空，自动推断
     * @return MinIO 可访问 URL
     */
    public String uploadFile(String prefix, String filename, InputStream inputStream, String contentType) {
        String filePath = builderFilePath(prefix, filename);
        try {
            if (contentType == null || contentType.isEmpty()) {
                if (filename.endsWith(".mp4") || filename.endsWith(".mov")) {
                    contentType = "video/mp4";
                } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
                    contentType = "image/jpeg";
                } else if (filename.endsWith(".html")) {
                    contentType = "text/html";
                } else {
                    contentType = "application/octet-stream";
                }
            }

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minIOConfigProperties.getBucket())
                    .object(filePath)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);

            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator).append(minIOConfigProperties.getBucket()).append(separator).append(filePath);

            return urlPath.toString();
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败", ex);
        }
    }




}
