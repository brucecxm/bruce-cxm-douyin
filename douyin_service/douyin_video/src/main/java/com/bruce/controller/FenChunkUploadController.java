package com.bruce.controller;
import com.bruce.entity.VideoInfo;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.VideoInfoService;
import com.bruce.utils.FfmpegUtils;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.minio.messages.Item;

import java.nio.file.Path;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class FenChunkUploadController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private VideoInfoService videoInfoService;



    @Autowired
    private FfmpegUtils ffmpegUtils;  // 注入FFmpeg工具类

    @Value("${video.temp.dir}")
    private String videoTempDir;  // 本地临时目录

    private final String BUCKET = "douyin";
    // HLS文件存储路径（MinIO中）
    private final String HLS_PREFIX = "testfile/hls/";  // .m3u8和.ts存放的根路径

    @Autowired
    private FileStorageService fileStorageService; // 注入 MinIOFileStorageService

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
     * 改造核心：合并分片 + 转换为HLS + 上传.m3u8和.ts
     */
    @PostMapping("/merge")
    public Map<String, Object> mergeChunks(@RequestBody Map<String, Object> param) throws Exception {
        String fileName = (String) param.get("fileName");
        String fileHash = (String) param.get("fileHash");
        Integer total = (Integer) param.get("total");
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 合并后的完整视频在MinIO中的路径（原始视频，转换后可删除）
        String originalObjectName = "testfile/" + datePath + "/" + fileName;


        // -------------------------- 步骤1：合并分片为完整视频（原有逻辑） --------------------------
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            String partObjectName = "testfile/temp/" + fileHash + "/" + i + ".part";
            sources.add(ComposeSource.builder()
                    .bucket(BUCKET)
                    .object(partObjectName)
                    .build());
        }
        // 服务端合并分片
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(originalObjectName)
                        .sources(sources)
                        .build()
        );


        // -------------------------- 新增步骤：将完整视频转换为HLS格式 --------------------------
        // 1. 创建本地临时目录（存放转换过程文件）
//        String localTempDir = videoTempDir + fileHash + "/";
        Path localTempPath = Paths.get(videoTempDir, fileHash);
        String localTempDir = localTempPath.toString();
        Files.createDirectories(Paths.get(localTempDir));
        // 本地临时保存的完整视频路径
        String localOriginalPath = localTempDir + "original." + getFileExtension(fileName);


        // 2. 将MinIO中的完整视频下载到本地临时目录
        try (InputStream in = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(originalObjectName)
                        .build());
             OutputStream out = Files.newOutputStream(Paths.get(localOriginalPath))
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }


        // 3. 调用FFmpeg转换为HLS（生成.m3u8和.ts）
        String hlsLocalDir = localTempDir + "hls/";  // 本地存放HLS文件的目录
        ffmpegUtils.convertToHls(localOriginalPath, hlsLocalDir);


        // 4. 将HLS文件（.m3u8和.ts）上传到MinIO
        String hlsMinioDir = HLS_PREFIX + fileHash + "/";  // MinIO中HLS文件的路径
        uploadLocalDirToMinIO(hlsLocalDir, hlsMinioDir);


        // -------------------------- 清理资源 --------------------------
        // 删除本地临时文件（避免占用磁盘）
        deleteLocalDir(localTempDir);
        // 可选：删除MinIO中的原始视频（节省空间，只保留HLS文件）
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(BUCKET)
                        .object(originalObjectName)
                        .build()
        );
        // 删除临时分片（原有逻辑）
        deleteTempParts(fileHash);





        // -------------------------- 新增：将视频信息存入数据库 --------------------------
        // 1. 获取视频元信息（时长、大小等）
        long fileSize = new File(localOriginalPath).length(); // 本地原始文件大小
        int duration = ffmpegUtils.getVideoDuration(localOriginalPath); // 需实现FFmpeg获取时长的方法

        // 2. 构建VideoInfo实体
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setVideoTitle(fileName); // 用文件名作为标题（可替换为前端传入的标题）
        videoInfo.setFileHash(fileHash);
        videoInfo.setM3u8MinioPath(hlsMinioDir + "index.m3u8"); // 存MinIO中的路径，不存带签名的URL
        videoInfo.setDuration(duration);
        videoInfo.setFileSize(fileSize);
        videoInfo.setUserId(1L); // 这里简化为固定用户ID，实际应从登录信息中获取
        videoInfo.setStatus(1); // 状态：可用（转码完成）

        // 3. 存入数据库
        videoInfoService.save(videoInfo);
        // -------------------------- 构建返回结果 --------------------------
        Map<String, Object> result = new HashMap<>();
        result.put("message", "合并并转换HLS完成");
        result.put("originalUrl", originalObjectName);  // 原始视频路径（可选，已删除则返回null）
        result.put("m3u8Url", hlsMinioDir + "index.m3u8");  // .m3u8索引文件路径（前端播放用）
        result.put("m3u8AccessUrl", generatePresignedUrl(hlsMinioDir + "index.m3u8"));  // 带签名的临时访问URL
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












    // -------------------------- 工具方法 --------------------------

    /**
     * 将本地目录下的文件批量上传到MinIO
     */
    private void uploadLocalDirToMinIO(String localDir, String minioDir) throws Exception {
        File dir = new File(localDir);
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                String minioObjectName = minioDir + file.getName();
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(BUCKET)
                                .object(minioObjectName)
                                .filename(file.getAbsolutePath())
                                .contentType(getContentType(file.getName()))
                                .build()
                );
            }
        }
    }

    /**
     * 生成MinIO文件的临时访问URL（有效期1小时）
     */
    private String generatePresignedUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(BUCKET)
                        .object(objectName)
                        .expiry(3600)  // 1小时有效期
                        .build()
        );
    }

    /**
     * 删除MinIO中的临时分片
     */
    private void deleteTempParts(String fileHash) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET)
                        .prefix("testfile/temp/" + fileHash + "/")
                        .recursive(true)
                        .build()
        );
        for (Result<Item> result : results) {
            Item item = result.get();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(item.objectName())
                            .build()
            );
        }
    }

    /**
     * 删除本地目录（递归删除）
     */
    private void deleteLocalDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteLocalDir(file.getAbsolutePath());
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    /**
     * 获取文件后缀（如mp4、mov）
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }

    /**
     * 根据文件名获取ContentType
     */
    private String getContentType(String fileName) {
        if (fileName.endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        } else if (fileName.endsWith(".ts")) {
            return "video/MP2T";
        } else {
            return "application/octet-stream";
        }
    }
}
