package com.bruce.controller;

import com.bruce.entity.FileOperationLog;
import com.bruce.file.service.FileStorageService;
import com.bruce.service.FileOperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private FileOperationLogService fileOperationLogService;

    @Mock
    private MockHttpServletRequest request;

    private FileController fileController;

    @BeforeEach
    void setUp() {
        // 初始化Mock对象
        MockitoAnnotations.initMocks(this);
        // 创建控制器实例
        fileController = new FileController();
        // 使用反射设置私有字段
        setPrivateField(fileController, "fileStorageService", fileStorageService);
        setPrivateField(fileController, "fileOperationLogService", fileOperationLogService);
        setPrivateField(fileController, "request", request);
        
        // 模拟IP地址
        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        String expectedUrl = "http://example.com/images/test.jpg";
        when(fileStorageService.uploadImgFile(anyString(), anyString(), any())).thenReturn(expectedUrl);

        // 执行测试
        ResponseEntity<?> response = fileController.uploadImage(file, "images");

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // 验证是否调用了日志保存方法
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
    }

    @Test
    void testUploadImageFailure() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(fileStorageService.uploadImgFile(anyString(), anyString(), any()))
                .thenThrow(new RuntimeException("Upload failed"));

        // 执行测试
        ResponseEntity<?> response = fileController.uploadImage(file, "images");

        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        // 验证是否调用了日志保存方法
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
    }

    @Test
    void testDeleteFileSuccess() {
        // 准备测试数据
        String fileUrl = "http://example.com/images/test.jpg";
        
        // 执行测试
        ResponseEntity<?> response = fileController.deleteFile(fileUrl);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // 验证是否调用了文件删除和日志保存方法
        verify(fileStorageService, times(1)).delete(fileUrl);
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
    }

    @Test
    void testDeleteFileEmptyUrl() {
        // 执行测试
        ResponseEntity<?> response = fileController.deleteFile("");

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        // 验证是否调用了日志保存方法
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
        // 验证没有调用文件删除方法
        verify(fileStorageService, never()).delete(anyString());
    }

    @Test
    void testDownloadFileSuccess() throws Exception {
        // 准备测试数据
        String fileUrl = "http://example.com/images/test.jpg";
        byte[] fileContent = "test file content".getBytes();
        when(fileStorageService.downLoadFile(fileUrl)).thenReturn(fileContent);

        // 执行测试
        ResponseEntity<?> response = fileController.downloadFile(fileUrl);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // 验证是否调用了日志保存方法
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
    }

    @Test
    void testDownloadFileNotFound() throws Exception {
        // 准备测试数据
        String fileUrl = "http://example.com/images/nonexistent.jpg";
        when(fileStorageService.downLoadFile(fileUrl)).thenReturn(null);

        // 执行测试
        ResponseEntity<?> response = fileController.downloadFile(fileUrl);

        // 验证结果
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        // 验证是否调用了日志保存方法
        verify(fileOperationLogService, times(1)).saveLog(any(FileOperationLog.class));
    }

    @Test
    void testBatchUploadImagesSuccess() throws Exception {
        // 准备测试数据
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                "image/jpeg",
                "test image 1 content".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "test2.jpg",
                "image/jpeg",
                "test image 2 content".getBytes()
        );
        MockMultipartFile[] files = {file1, file2};

        when(fileStorageService.uploadImgFile(anyString(), anyString(), any()))
                .thenReturn("http://example.com/images/test1.jpg")
                .thenReturn("http://example.com/images/test2.jpg");

        // 执行测试
        ResponseEntity<?> response = fileController.batchUploadImages(files, "images");

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // 验证是否为每个文件都调用了日志保存方法
        verify(fileOperationLogService, times(2)).saveLog(any(FileOperationLog.class));
    }
}