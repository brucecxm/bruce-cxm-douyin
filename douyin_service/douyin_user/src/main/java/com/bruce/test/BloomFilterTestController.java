package com.bruce.test;

import com.bruce.pojo.Result;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 布隆过滤器测试控制器
 * 用于测试修复后的布隆过滤器功能是否正常工作
 */
@RestController
@RequestMapping("/test/bloom-filter")
public class BloomFilterTestController {

    @Autowired
    private RBloomFilter<Long> userBloomFilter;

    /**
     * 测试用户ID是否在布隆过滤器中
     * 
     * @param userId 用户ID
     * @return 测试结果
     */
    /**
     * 测试用户ID是否在布隆过滤器中
     * 
     * @param userId 用户ID
     * @return 测试结果
     */
    @GetMapping("/contains")
    public Result<Boolean> testBloomFilter(@RequestParam Long userId) {
        boolean contains = userBloomFilter.contains(userId);
        return Result.success(contains);
    }

    /**
     * 手动添加用户ID到布隆过滤器
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @GetMapping("/add")
    public Result<String> addToBloomFilter(@RequestParam Long userId) {
        userBloomFilter.add(userId);
        return Result.success("用户ID已添加到布隆过滤器");
    }
}
