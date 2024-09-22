package com.bruce.config;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;
//静态工厂实例化bean 注解是取代了xml文件
//静态工厂实例化bean 注解是取代了xml文件
//静态工厂实例化bean 注解是取代了xml文件
//静态工厂实例化bean 注解是取代了xml文件
//这里用到了 MinioClient 的静态工厂方法 builder()，这是一种创建对象的方式，通过调用类的静态方法来创建和配置对象实例。
//            MinioClient.builder() 返回一个 MinioClient.Builder 对象，
//    然后通过链式调用设置 endpoint 和 credentials，最后调用 build() 方法来创建 MinioClient 的实例。
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
