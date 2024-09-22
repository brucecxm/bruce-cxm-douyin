package com.bruce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class DouyinIntroduceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DouyinIntroduceApplication.class);
    }
}
