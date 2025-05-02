package com.bruce.config;

import com.bruce.utils.SnowflakeIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(SnowflakeIdGenerator.class)
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public Long workerId() {
        return 1L;  // 示例值
    }

    @Bean
    public Long datacenterId() {
        return 1L;  // 示例值
    }
//todo 只要你用 @Bean 声明一个方法，Spring 会尝试自动装配该方法的所有参数。
//todo 只要你用 @Bean 声明一个方法，Spring 会尝试自动装配该方法的所有参数。
// 因此这里spring会在容器里找是否有Long类型的bean 名字叫做workerId和datacenterId 如果找不到就会报错  因此得用下面的构造方法
//    @Bean
//    public SnowflakeIdGenerator snowflakeIdGenerator(Long workerId, Long datacenterId) {
//        return new SnowflakeIdGenerator(workerId, datacenterId);
//    }

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(1L, 1L);
    }
}
