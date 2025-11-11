package com.bruce.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * 接口执行时间日志切面（记录UserController所有接口）
 */
@Aspect
@Component
public class UserControllerTimeLogAspect {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTimeLogAspect.class);

    /**
     * 切入点：匹配UserController中所有公共方法（即所有接口）
     * 注意：需替换包路径为实际项目中UserController的包路径
     */
    @Pointcut("execution(public * com.bruce.controller.UserController.*(..))")
    public void userControllerPointcut() {}

    /**
     * 环绕通知：记录接口执行时间、请求参数、URL等信息
     */
    @Around("userControllerPointcut()")
    public Object logInterfaceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 生成唯一请求ID（用于追踪一次请求）
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // 2. 获取请求相关信息（URL、方法名、参数等）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest(); // 获取当前请求对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod(); // 获取目标方法
        String methodName = method.getName(); // 方法名（如doLogin、register）
        String requestUrl = request.getRequestURL().toString(); // 请求完整URL
        String httpMethod = request.getMethod(); // 请求方式（GET/POST）
        Object[] args = joinPoint.getArgs(); // 方法参数

        // 3. 记录接口开始执行时间
        long startTime = System.currentTimeMillis();
        log.info(
                "[{}] 接口开始执行 - URL: {}, 方法: {}, HTTP方式: {}, 参数: {}",
                requestId, requestUrl, methodName, httpMethod, Arrays.toString(args)
        );

        try {
            // 4. 执行目标接口方法
            Object result = joinPoint.proceed();

            // 5. 计算执行耗时并记录日志
            long costTime = System.currentTimeMillis() - startTime;
            log.info(
                    "[{}] 接口执行完成 - 方法: {}, 耗时: {}ms, 响应结果: {}",
                    requestId, methodName, costTime, result
            );

            return result;
        } catch (Throwable e) {
            // 6. 若接口抛出异常，记录异常信息和耗时
            long costTime = System.currentTimeMillis() - startTime;
            log.error(
                    "[{}] 接口执行异常 - 方法: {}, 耗时: {}ms, 异常信息: {}",
                    requestId, methodName, costTime, e.getMessage(), e
            );
            throw e; // 继续抛出异常，不影响原有业务逻辑的异常处理
        }
    }
}
