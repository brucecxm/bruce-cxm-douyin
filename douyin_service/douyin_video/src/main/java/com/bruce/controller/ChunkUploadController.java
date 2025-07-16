package com.bruce.controller;

import com.bruce.file.service.FileStorageService;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import io.minio.messages.Item;

@RestController
@RequestMapping("/uploadbefore")
public class ChunkUploadController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileStorageService fileStorageService; // 注入 MinIOFileStorageService

    private final String BUCKET = "douyin"; // 可替换成你的 bucket

    /**
     * 查询已上传分片（在 MinIO 中查询已存在的 .part 文件）
     */
    @GetMapping("/check")
    public Map<String, Object> checkUploadedChunks(@RequestParam String fileHash) throws Exception {
        List<Integer> uploaded = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET)
                        .prefix("testfile/temp/" + fileHash + "/")
                        .recursive(true)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            if (objectName.endsWith(".part")) {
                String partName = objectName.substring(objectName.lastIndexOf("/") + 1).replace(".part", "");
                uploaded.add(Integer.parseInt(partName));
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uploaded", uploaded);
        return map;
    }

    /**
     * 上传分片（直接上传到 MinIO）
     */
    @PostMapping("/chunk")
    public String uploadChunk(@RequestParam("file") MultipartFile file,
                              @RequestParam("index") Integer index,
                              @RequestParam("fileHash") String fileHash) throws Exception {

        String objectName = "testfile/temp/" + fileHash + "/" + index + ".part";

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType("application/octet-stream")
                        .build()
        );

        return "分片 " + index + " 上传成功";
    }


    /**
     * 合并分片（使用 MinIO 服务端合并功能，无需下载到本地）
     */
    @PostMapping("/merge")
    public Map<String, Object> mergeChunks(@RequestBody Map<String, Object> param) throws Exception {
        String fileName = (String) param.get("fileName");
        String fileHash = (String) param.get("fileHash");
        Integer total = (Integer) param.get("total");
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String finalObjectName = "testfile/" + datePath + "/" + fileName;

        // 构建合并源对象列表
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            String partObjectName = "testfile/temp/" + fileHash + "/" + i + ".part";
            sources.add(ComposeSource.builder()
                    .bucket(BUCKET)
                    .object(partObjectName)
                    .build());
        }

        // 服务端直接合并对象
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(finalObjectName)
                        .sources(sources)
                        .build()
        );

        // 生成访问 URL（根据实际需求调整）
//        String minioUrl = fileStorageService.generatePresignedUrl(BUCKET, finalObjectName);
        String minioUrl ="test";

        // 删除临时分片
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET)
                        .prefix("testfile/temp/" + fileHash + "/")
                        .recursive(true)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(objectName)
                    .build());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "服务端合并完成，无需下载到本地");
        result.put("minioUrl", minioUrl);
        return result;
    }






    /**
     * 合并分片（从 MinIO 拉取分片拼接后直接上传回 MinIO）
     */
    @PostMapping("/mergebefore")
    public Map<String, Object> mergeChunksbefore(@RequestBody Map<String, Object> param) throws Exception {
        String fileName = (String) param.get("fileName");
        String fileHash = (String) param.get("fileHash");
        Integer total = (Integer) param.get("total");

        // 创建管道和线程安全拼接流
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        Thread mergeThread = new Thread(() -> {
            try (OutputStream outputStream = pos) {
                for (int i = 0; i < total; i++) {
                    String objectName = "testfile/temp/" + fileHash + "/" + i + ".part";
                    try (InputStream is = minioClient.getObject(GetObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(objectName)
                            .build())) {
                        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
            } catch (Exception e) {
                    throw new RuntimeException(e);
                    }
                    });

                    mergeThread.start();

                    // 上传合并后的文件流到 MinIO testfile/yyyy/MM/dd/filename
                    String minioUrl = fileStorageService.uploadFile("testfile", fileName, pis, null);

                    mergeThread.join();

                    // 删除临时分片
                    Iterable<Result<Item>> results = minioClient.listObjects(
        ListObjectsArgs.builder()
        .bucket(BUCKET)
        .prefix("testfile/temp/" + fileHash + "/")
        .recursive(true)
        .build()
        );

        for (Result<Item> result : results) {
        Item item = result.get();
        String objectName = item.objectName();
        minioClient.removeObject(RemoveObjectArgs.builder()
        .bucket(BUCKET)
        .object(objectName)
        .build());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "上传并合并完成，已上传至 MinIO");
        result.put("minioUrl", minioUrl);
        return result;
    }
}
