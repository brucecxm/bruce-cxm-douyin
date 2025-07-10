package com.bruce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/down")
public class FileDownloadController {


    private static String storagePath="C:/Users/Administrator/Desktop/test";

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
//    Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
//    Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
//    Spring Boot 默认路径变量匹配规则会截断 . 后内容。 因此这里要加上这个防止阶段后缀
    @GetMapping("/files/{fileName:.+}")
    public void downloadFile(
            @PathVariable("fileName") String fileName,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
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
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
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

            while (bytesRemaining > 0 && (bytesRead = in.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                out.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            out.flush();
        }
    }
}