package com.bruce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/down")
public class FileDownloadController {

    private static String storagePath = "f:/GitHub/bruce-cxm-douyin - 副本/doc/简历";

    /**
     * 获取文件信息（用于前端初始化下载）
     */
    @GetMapping("/files/{fileName}/info")
    // JDK 1.8 兼容版本
    public Map<String, Object> getFileInfo(@PathVariable String fileName) {
        Path filePath = Paths.get(storagePath, fileName);
        if (!Files.exists(filePath)) {
            // 使用 HashMap 替代 Map.of()
            Map<String, Object> result = new HashMap<>();
            result.put("exists", false);
            result.put("message", "文件不存在");
            return result;
        }

        try {
            long fileSize = Files.size(filePath);
            // 使用 HashMap 替代 Map.of()
            Map<String, Object> result = new HashMap<>();
            result.put("exists", true);
            result.put("fileName", fileName);
            result.put("fileSize", fileSize);
            result.put("message", "文件信息获取成功");
            return result;
        } catch (IOException e) {
            // 使用 HashMap 替代 Map.of()
            Map<String, Object> result = new HashMap<>();
            result.put("exists", false);
            result.put("message", "获取文件大小失败");
            return result;
        }
    }

    /**
     * 分片下载接口（支持断点续传）
     */
    // Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
    // Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
    // Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
    @GetMapping("/files/{fileName:.+}")
    public void downloadFile(
            @PathVariable("fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 构建完整文件路径
        Path filePath = Paths.get(storagePath, fileName);
        if (!Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
            return;
        }

        File file = filePath.toFile();
        long fileSize = file.length();

        // 1. 解析Range请求头
        String rangeHeader = request.getHeader("Range");
        long start = 0;
        long end = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] rangeParts = rangeHeader.substring("bytes=".length()).split("-");
            start = Long.parseLong(rangeParts[0]);
            if (rangeParts.length > 1 && !rangeParts[1].isEmpty()) {
                end = Long.parseLong(rangeParts[1]);
            }
        }

        // 2. 校验Range合法性
        if (start < 0 || start >= fileSize) {
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }
        end = Math.min(end, fileSize - 1);

        // 3. 设置响应头
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentLengthLong(end - start + 1);

        // 4. 流式传输文件分片
        try (InputStream in = new FileInputStream(file);
                OutputStream out = response.getOutputStream()) {

            // 跳过已不需要的字节
            in.skip(start);

            byte[] buffer = new byte[8192];
            long bytesRemaining = end - start + 1;
            int bytesRead;

            while (bytesRemaining > 0
                    && (bytesRead = in.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                out.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            out.flush();
        }
    }

    /**
     * 简历上传接口
     */
    @PostMapping("/api/resume/upload")
    public Map<String, Object> uploadResume(
            @RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "上传文件不能为空");
                return result;
            }

            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.matches(".*\\.(docx|pdf)$")) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "只能上传docx或pdf文件");
                return result;
            }

            // 保存文件到指定目录，使用固定文件名
            String targetFilename = "陈雪明的简历.docx";
            Path targetPath = Paths.get(storagePath, targetFilename);

            // 确保目录存在
            Files.createDirectories(targetPath.getParent());

            // 保存文件，覆盖已存在的文件
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "简历上传成功");
            result.put("data", getResumeInfo());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "简历上传失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 获取简历信息
     */
    @GetMapping("/api/resume/info")
    public Map<String, Object> getResumeInfo() {
        try {
            String filename = "陈雪明的简历.docx";
            Path filePath = Paths.get(storagePath, filename);

            if (!Files.exists(filePath)) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "简历不存在");
                return result;
            }

            long fileSize = Files.size(filePath);
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();

            Map<String, Object> resumeInfo = new HashMap<>();
            resumeInfo.put("fileName", filename);
            resumeInfo.put("fileSize", fileSize);
            resumeInfo.put("uploadTime", new java.util.Date(lastModified).toString());

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "获取简历信息成功");
            result.put("data", resumeInfo);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "获取简历信息失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 更新简历接口（与上传接口功能相同，用于语义化区分）
     */
    @PostMapping("/api/resume/update")
    public Map<String, Object> updateResume(
            @RequestParam("file") MultipartFile file) {
        // 复用上传接口的逻辑
        return uploadResume(file);
    }
}