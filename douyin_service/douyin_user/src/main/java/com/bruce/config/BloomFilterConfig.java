//package com.bruce.config;
//import org.redisson.api.RBloomFilter;
//import org.redisson.api.RedissonClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class BloomFilterConfig {
//
//    @Bean
//    public RBloomFilter<Long> userBloomFilter(RedissonClient redissonClient) {
//        // 初始化布隆过滤器，预计元素数量100万，误判率0.01
//        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter("userBloomFilter");
//        bloomFilter.tryInit(1000000L, 0.01);
//
//        // 这里需要从数据库加载所有用户ID到布隆过滤器
//        // userService.list().forEach(user -> bloomFilter.add(user.getId()));
//
//        return bloomFilter;
//    }
//}
