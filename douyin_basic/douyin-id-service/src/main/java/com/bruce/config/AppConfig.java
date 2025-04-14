package com.bruce.config;

import com.bruce.utils.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Long workerId() {
        return 1L;  // 示例值
    }

    @Bean
    public Long datacenterId() {
        return 1L;  // 示例值
    }

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator(Long workerId, Long datacenterId) {
        return new SnowflakeIdGenerator(workerId, datacenterId);
    }
}
