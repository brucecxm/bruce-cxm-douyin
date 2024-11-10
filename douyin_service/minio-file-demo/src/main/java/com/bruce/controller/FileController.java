package com.bruce.controller;
import com.bruce.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;


    @GetMapping("/test")
    public void downloadFileone() {
       System.out.println("dddddddd");
    }







    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            fileService.uploadFile(file.getOriginalFilename(), inputStream, file.getSize());
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/{objectName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String objectName) {
        try (InputStream inputStream = fileService.downloadFile(objectName)) {
            byte[] bytes = readInputStream(inputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
