package com.bruce.controller;

import com.bruce.file.service.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * 提供全面的文件上传、下载、删除等功能
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理接口")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传图片文件
     * @param file 图片文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    @ApiOperation("上传图片文件")
    @PostMapping("/upload/image")

    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "prefix", required = false, defaultValue = "images") String prefix) {
        return uploadFile(file, prefix, "image");
    }

    /**
     * 上传视频文件
     * @param file 视频文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    @ApiOperation("上传视频文件")
    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "prefix", required = false, defaultValue = "videos") String prefix) {
        return uploadFile(file, prefix, "video");
    }

    /**
     * 上传HTML文件
     * @param file HTML文件
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含文件URL
     */
    @ApiOperation("上传HTML文件")
    @PostMapping("/upload/html")
    public ResponseEntity<?> uploadHtml(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "prefix", required = false, defaultValue = "htmls") String prefix) {
        return uploadFile(file, prefix, "html");
    }

    /**
     * 通用文件上传
     * @param file 文件
     * @param prefix 文件前缀，用于分类存储
     * @param fileType 文件类型(image/video/html/other)
     * @return 上传结果，包含文件URL
     */
    @ApiOperation("通用文件上传")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAnyFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "prefix", required = false, defaultValue = "files") String prefix,
                                         @RequestParam(value = "fileType", required = false, defaultValue = "other") String fileType) {
        return uploadFile(file, prefix, fileType);
    }

    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @return 删除结果
     */
    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            if (StringUtils.isEmpty(fileUrl)) {
                return ResponseEntity.badRequest().body("文件URL不能为空");
            }

            fileStorageService.delete(fileUrl);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "文件删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("删除文件失败: {}", e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "文件删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件URL
     * @return 文件流
     */
    @ApiOperation("下载文件")
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            if (StringUtils.isEmpty(fileUrl)) {
                return ResponseEntity.badRequest().body("文件URL不能为空");
            }

            byte[] data = fileStorageService.downLoadFile(fileUrl);
            if (data == null || data.length == 0) {
                return ResponseEntity.notFound().build();
            }

            // 从URL中提取文件名
            String fileName = extractFileName(fileUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("下载文件失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 批量上传图片文件
     * @param files 图片文件数组
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含多个文件URL
     */
    @ApiOperation("批量上传图片文件")
    @PostMapping("/upload/images/batch")
    public ResponseEntity<?> batchUploadImages(@RequestParam("files") MultipartFile[] files,
                                              @RequestParam(value = "prefix", required = false, defaultValue = "images") String prefix) {
        return batchUploadFiles(files, prefix, "image");
    }

    /**
     * 批量上传视频文件
     * @param files 视频文件数组
     * @param prefix 文件前缀，用于分类存储
     * @return 上传结果，包含多个文件URL
     */
    @ApiOperation("批量上传视频文件")
    @PostMapping("/upload/videos/batch")
    public ResponseEntity<?> batchUploadVideos(@RequestParam("files") MultipartFile[] files,
                                             @RequestParam(value = "prefix", required = false, defaultValue = "videos") String prefix) {
        return batchUploadFiles(files, prefix, "video");
    }

    /**
     * 批量上传文件
     * @param files 文件数组
     * @param prefix 文件前缀，用于分类存储
     * @param fileType 文件类型
     * @return 上传结果，包含多个文件URL
     */
    @ApiOperation("批量上传通用文件")
    @PostMapping("/upload/batch")
    public ResponseEntity<?> batchUploadAnyFiles(@RequestParam("files") MultipartFile[] files,
                                               @RequestParam(value = "prefix", required = false, defaultValue = "files") String prefix,
                                               @RequestParam(value = "fileType", required = false, defaultValue = "other") String fileType) {
        return batchUploadFiles(files, prefix, fileType);
    }

    /**
     * 内部方法：单个文件上传处理
     */
    private ResponseEntity<?> uploadFile(MultipartFile file, String prefix, String fileType) {
        try {
            validateFile(file);

            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String newFilename = generateUniqueFilename(originalFilename);

            String fileUrl;
            try (InputStream inputStream = file.getInputStream()) {
                switch (fileType.toLowerCase()) {
                    case "image":
                        fileUrl = fileStorageService.uploadImgFile(prefix, newFilename, inputStream);
                        break;
                    case "video":
                        fileUrl = fileStorageService.uploadVideoFile(prefix, newFilename, inputStream);
                        break;
                    case "html":
                        fileUrl = fileStorageService.uploadHtmlFile(prefix, newFilename, inputStream);
                        break;
                    default:
                        fileUrl = fileStorageService.uploadFile(prefix, newFilename, inputStream, file.getContentType());
                        break;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("fileUrl", fileUrl);
            result.put("filename", originalFilename);
            result.put("size", file.getSize());
            result.put("type", file.getContentType());

            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.warn("文件验证失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("文件验证失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 内部方法：批量文件上传处理
     */
    private ResponseEntity<?> batchUploadFiles(MultipartFile[] files, String prefix, String fileType) {
        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.badRequest().body("请选择要上传的文件");
            }

            // 限制批量上传文件数量
            if (files.length > 10) {
                return ResponseEntity.badRequest().body("单次批量上传文件数不能超过10个");
            }

            Map<String, Object> result = new HashMap<>();
            Map<String, String> fileResults = new HashMap<>();
            int successCount = 0;
            int failCount = 0;

            for (MultipartFile file : files) {
                try {
                    validateFile(file);

                    String originalFilename = file.getOriginalFilename();
                    String newFilename = generateUniqueFilename(originalFilename);

                    String fileUrl;
                    try (InputStream inputStream = file.getInputStream()) {
                        switch (fileType.toLowerCase()) {
                            case "image":
                                fileUrl = fileStorageService.uploadImgFile(prefix, newFilename, inputStream);
                                break;
                            case "video":
                                fileUrl = fileStorageService.uploadVideoFile(prefix, newFilename, inputStream);
                                break;
                            default:
                                fileUrl = fileStorageService.uploadFile(prefix, newFilename, inputStream, file.getContentType());
                                break;
                        }
                    }

                    fileResults.put(originalFilename, fileUrl);
                    successCount++;
                } catch (Exception e) {
                    log.error("文件 {} 上传失败: {}", file.getOriginalFilename(), e.getMessage());
                    fileResults.put(file.getOriginalFilename(), "上传失败: " + e.getMessage());
                    failCount++;
                }
            }

            result.put("success", true);
            result.put("total", files.length);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("fileResults", fileResults);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("批量文件上传失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("批量文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 验证文件大小（可根据需要调整）
        long maxSize = 100 * 1024 * 1024; // 100MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小不能超过100MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFilename(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String baseName = StringUtils.stripFilenameExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.hasLength(extension)) {
            return baseName + "_" + uuid + "." + extension;
        }
        return baseName + "_" + uuid;
    }

    /**
     * 从URL中提取文件名
     */
    private String extractFileName(String fileUrl) {
        String[] parts = fileUrl.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return "download_file";
    }
}
