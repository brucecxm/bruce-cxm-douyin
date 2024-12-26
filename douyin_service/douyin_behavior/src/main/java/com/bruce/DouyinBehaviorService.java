package com.bruce;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Hai
 * @date 2020/6/16 - 12:45
 */
@SpringBootApplication
@EnableScheduling
public class DouyinBehaviorService {
  public static void main(String[] args) {
    SpringApplication.run(DouyinBehaviorService.class, args);
  }
}
