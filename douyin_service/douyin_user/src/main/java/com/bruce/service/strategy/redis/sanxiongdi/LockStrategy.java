package com.bruce.service.strategy.redis.sanxiongdi;

import com.bruce.entity.User;
import com.bruce.entity.UserInfo;
import com.bruce.entity.Video;
import com.bruce.service.strategy.redis.sanxiongdi.CachingStrategy;
import com.bruce.dao.UserDao;
import com.bruce.service.VideoFeignService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("lock")
public class LockStrategy implements CachingStrategy {
    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoFeignService videoFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserInfo getUserInfo(Long userId, Integer page, Integer pageSize) {
        // 构建缓存键
        String key = "user_info:" + userId;
        String emptyKey = "empty_user:" + userId;

        // 尝试从缓存获取
        try {
            String userJson = stringRedisTemplate.opsForValue().get(key);
            if (userJson != null) {
                // 反序列化用户信息
                UserInfo userInfo = objectMapper.readValue(userJson, UserInfo.class);
                return userInfo;
            }

            // 检查空值缓存
            String emptyValue = stringRedisTemplate.opsForValue().get(emptyKey);
            if ("null".equals(emptyValue)) {
                return null;
            }
        } catch (Exception e) {
            // Redis操作失败，继续查询数据库
        }

        // 获取分布式锁
        RLock lock = redissonClient.getLock("user_info_lock:" + userId);
        boolean lockAcquired = false;

        try {
            // 尝试获取锁，最多等待5秒，锁自动释放时间30秒
            lockAcquired = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (lockAcquired) {
                // 双重检查缓存
                try {
                    String userJson = stringRedisTemplate.opsForValue().get(key);
                    if (userJson != null) {
                        // 反序列化用户信息
                        UserInfo userInfo = objectMapper.readValue(userJson, UserInfo.class);
                        return userInfo;
                    }
                } catch (Exception e) {
                    // Redis操作失败，继续查询数据库
                }

                // 从数据库查询用户信息
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getId, userId);
                User user = userDao.selectOne(queryWrapper);
                if (user == null) {
                    // 设置空值缓存
                    try {
                        stringRedisTemplate.opsForValue().set(emptyKey, "null", 5, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        // Redis操作失败，不影响主流程
                    }
                    return null;
                }

                // 构建UserInfo对象
                UserInfo userInfo = new UserInfo();
                // 复制用户基本信息
                BeanUtils.copyProperties(user, userInfo);

                // 查询视频信息
                try {
                    if (page != null && pageSize != null) {
                        Page<Video> pageQuery = new Page<>(page, pageSize);
                        LambdaQueryWrapper<Video> videoQueryWrapper = new LambdaQueryWrapper<>();
                        videoQueryWrapper.eq(Video::getUserId, userId);
                        IPage<Video> videoPage = videoFeignService.page(pageQuery, videoQueryWrapper);
                        userInfo.setVideoPage(videoPage);
                    }
                } catch (Exception e) {
                    // 视频查询失败，不影响用户信息返回
                }

                // 更新缓存
                try {
                    // 序列化用户信息
                    String userJson = objectMapper.writeValueAsString(userInfo);
                    stringRedisTemplate.opsForValue().set(key, userJson, 10, TimeUnit.MINUTES);
                    // 删除空值缓存
                    stringRedisTemplate.delete(emptyKey);
                } catch (Exception e) {
                    // Redis操作失败，不影响主流程
                }

                return userInfo;
            }
        } catch (InterruptedException e) {
            // 获取锁失败，中断线程
            Thread.currentThread().interrupt();
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
        }

        // 获取锁失败，尝试直接查询数据库
        try {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getId, userId);
            User user = userDao.selectOne(queryWrapper);
            if (user == null) {
                return null;
            }

            // 构建UserInfo对象
            UserInfo userInfo = new UserInfo();
            // 复制用户基本信息
            BeanUtils.copyProperties(user, userInfo);

            return userInfo;
        } catch (Exception e) {
            // 查询失败，返回null
            return null;
        }
    }
}
