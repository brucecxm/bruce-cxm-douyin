package com.bruce.aop;
import com.bruce.annotation.RateLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Before("@annotation(rateLimit)")
    public void rateLimit(JoinPoint joinPoint, RateLimit rateLimit) throws Exception {
        String ipAddress = getClientIP(); // 获取请求的IP地址，自行实现
        String key = "rate_limit_" + ipAddress;
        int limit = rateLimit.value();
        int period = rateLimit.period();

        // 判断是否在限定时间内已达到访问次数限制
        if (redisTemplate.opsForValue().get(key) == null) {
            redisTemplate.opsForValue().set(key, "1", period, TimeUnit.SECONDS);
        } else {
            int count = Integer.parseInt(redisTemplate.opsForValue().get(key));
            if (count > limit) {
                throw new Exception("IP访问次数超过限制");
            } else {
                redisTemplate.opsForValue().increment(key, 1);
            }
        }
    }

    // 获取客户端IP地址，这里需要根据具体的框架和环境来实现
    private String getClientIP() {
        // 实现获取IP地址的逻辑，例如从HttpServletRequest中获取
        return "127.0.0.1";
    }
}
