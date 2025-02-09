package com.bruce;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.bruce.mapper")
public class DouyinTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DouyinTestApplication.class,args);
        log.info("项目启动成功...");
    }
}
