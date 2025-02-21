package com.bruce.controller;

import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    @Value("${upload.temp.dir}")
    private String tempDir;

    // 检查分片状态
    @GetMapping("/check")
    public ResponseEntity<?> checkChunk(@RequestParam String hash) {
        File chunkDir = new File(tempDir + File.separator + hash);
        if (!chunkDir.exists()) return ResponseEntity.ok(Collections.emptyList());

        int[] uploaded = Arrays.stream(chunkDir.listFiles())
                .mapToInt(f -> Integer.parseInt(f.getName()))
                .toArray();
        return ResponseEntity.ok(uploaded);
    }

    // 分片上传
    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam String hash,
            @RequestParam int index
    ) throws IOException {
        File chunkDir = new File(tempDir + File.separator + hash);
        if (!chunkDir.exists()) chunkDir.mkdirs();

        File chunkFile = new File(chunkDir, String.valueOf(index));
        file.transferTo(chunkFile);
        return ResponseEntity.ok().build();
    }

    // 合并分片
    @PostMapping("/merge")
    public ResponseEntity<?> mergeChunks(
            @RequestBody MergeRequest request
    ) throws IOException {
        File chunkDir = new File(tempDir + File.separator + request.getHash());
        File outputFile = new File("uploads", request.getFilename());

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (int i = 0; i < chunkDir.listFiles().length; i++) {
                File chunk = new File(chunkDir, String.valueOf(i));
                Files.copy(chunk.toPath(), fos);
            }
        }
        FileUtils.deleteDirectory(chunkDir); // 清理临时文件
        return ResponseEntity.ok().build();
    }

    @Data
    static class MergeRequest {
        private String hash;
        private String filename;
    }
}
