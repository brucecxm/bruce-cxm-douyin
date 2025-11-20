package com.bruce.init;

import com.bruce.entity.User;
import com.bruce.entity.UserInfo;
import com.bruce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息预热器
 * 服务启动时预热高频用户数据到Redis缓存
 */
@Component
public class UserInfoPreloader {

    private static final Logger log = LoggerFactory.getLogger(UserInfoPreloader.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 服务启动时执行用户数据预热
     */
    @PostConstruct
    public void preloadHotUsers() {
        log.info("开始预热高频用户数据...");
        try {
            // 1. 获取高频用户ID列表（从数据库查询前1000名活跃用户）
            List<Long> hotUserIds = userService.getHotUserIds(1000);
            log.info("获取到{}个高频用户ID需要预热", hotUserIds.size());

            // 2. 多线程并行加载到缓存
            ExecutorService executor = Executors.newFixedThreadPool(10);  // 10个线程并行
            for (Long userId : hotUserIds) {
                executor.submit(() -> {
                    try {
                        // 查询用户信息
                        User user = userService.getById(userId);
                        if (user != null) {
                            // 使用与UserController一致的缓存键格式
                            String key = "user_info:" + userId;
                            
                            // 序列化为JSON字符串存储
                            String userJson = objectMapper.writeValueAsString(user);
                            stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
                            
                            log.debug("成功预热用户ID: {} 到缓存", userId);
                        }
                    } catch (Exception e) {
                        log.error("预热用户ID: {} 失败", userId, e);
                    }
                });
            }
            
            // 关闭线程池
            executor.shutdown();
            log.info("用户数据预热任务已提交，正在异步执行");
        } catch (Exception e) {
            log.error("用户数据预热失败", e);
        }
    }
}
