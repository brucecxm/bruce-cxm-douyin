package com.bruce.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/downminio")
public class MinioFileDownloadController {

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Value("${minio.bucket}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

    /**
     * 获取文件信息（用于前端初始化下载）
     */
    @GetMapping("/files/{fileName}/info")
    public Map<String, Object> getFileInfo(@PathVariable String fileName) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 查询文件元信息
            ObjectStat stat = minioClient.statObject(bucketName, fileName);
            long fileSize = stat.length();


            result.put("exists", true);
            result.put("fileName", fileName);
            result.put("fileSize", fileSize);
            result.put("message", "文件信息获取成功");
        } catch (MinioException e) {
            result.put("exists", false);
            result.put("message", "文件不存在或访问失败");
        } catch (Exception e) {
            result.put("exists", false);
            result.put("message", "未知错误：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分片下载接口（支持断点续传）
     */
    @GetMapping("/files/{fileName:.+}")
    public void downloadFile(
            @PathVariable("fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // 获取文件大小
            ObjectStat stat = minioClient.statObject(bucketName, fileName);
            long fileSize = stat.length();


            // 解析Range请求头
            String rangeHeader = request.getHeader("Range");
            long start = 0;
            long end = fileSize - 1;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            if (start < 0 || start >= fileSize) {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }
            end = Math.min(end, fileSize - 1);

            long contentLength = end - start + 1;

            // 设置响应头
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
            response.setContentLengthLong(contentLength);

            // 通过MinIO按范围获取对象输入流
            try (InputStream in = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .offset(start)
                            .length(contentLength)
                            .build());
                 // 输出到响应流
                 java.io.OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        } catch (MinioException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
