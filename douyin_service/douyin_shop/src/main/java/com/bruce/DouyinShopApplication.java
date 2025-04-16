package com.bruce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableFeignClients  // 启用 Feign 客户端
@EnableTransactionManagement
public class DouyinShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(DouyinShopApplication.class,args);
        log.info("项目启动成功...");
    }
}
