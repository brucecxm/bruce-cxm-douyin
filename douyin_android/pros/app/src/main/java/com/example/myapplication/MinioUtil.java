package com.example.myapplication;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO 工具类，用于操作 MinIO 对象存储
 */
public class MinioUtil {
    private static final String TAG = "MinioUtil";
    
    // MinIO 配置
    private static final String MINIO_ENDPOINT = "http://minio.example.com";
    private static final String MINIO_ACCESS_KEY = "your-access-key";
    private static final String MINIO_SECRET_KEY = "your-secret-key";
    private static final String MINIO_BUCKET = "frontend-assets";
    
    private static MinioClient minioClient;
    
    /**
     * 初始化 MinIO 客户端
     */
    public static void init() {
        if (minioClient == null) {
            try {
                minioClient = MinioClient.builder()
                        .endpoint(MINIO_ENDPOINT)
                        .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
                        .build();
                
                // 检查并创建桶
                if (!bucketExists(MINIO_BUCKET)) {
                    makeBucket(MINIO_BUCKET);
                }
                
                Log.d(TAG, "MinIO 客户端初始化成功");
            } catch (Exception e) {
                Log.e(TAG, "MinIO 客户端初始化失败", e);
            }
        }
    }
    
    /**
     * 检查桶是否存在
     */
    public static boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            Log.e(TAG, "检查桶是否存在失败: " + bucketName, e);
            return false;
        }
    }
    
    /**
     * 创建桶
     */
    public static boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            Log.d(TAG, "桶创建成功: " + bucketName);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "桶创建失败: " + bucketName, e);
            return false;
        }
    }
    
    /**
     * 获取对象输入流
     */
    @Nullable
    public static InputStream getObject(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            Log.e(TAG, "获取对象失败: " + objectName, e);
            return null;
        }
    }
    
    /**
     * 下载版本信息文件
     */
    @Nullable
    public static InputStream getVersionInfo() {
        return getObject(MINIO_BUCKET, "version.json");
    }
    
    /**
     * 下载指定版本的资源包
     */
    @Nullable
    public static InputStream downloadVersionPackage(String version) {
        String objectName = version + ".zip";
        return getObject(MINIO_BUCKET, objectName);
    }
    
    /**
     * 上传文件到 MinIO
     */
    public static boolean uploadFile(String bucketName, String objectName, InputStream inputStream, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
            Log.d(TAG, "文件上传成功: " + objectName);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "文件上传失败: " + objectName, e);
            return false;
        }
    }
    
    /**
     * 列出桶中的对象
     */
    public static void listObjects(String bucketName) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            
            for (Result<Item> result : results) {
                Item item = result.get();
                Log.d(TAG, "对象: " + item.objectName() + ", 大小: " + item.size());
            }
        } catch (Exception e) {
            Log.e(TAG, "列出对象失败: " + bucketName, e);
        }
    }
}
