package com.bruce;

import com.bruce.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
@EnableFeignClients
public class DouyinAdminService {
    public static void main(String[] args) {
        SpringApplication.run(DouyinAdminService.class, args);
    }
}
