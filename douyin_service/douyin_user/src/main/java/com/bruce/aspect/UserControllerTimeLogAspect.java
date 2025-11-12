package com.bruce.aspect;

import com.bruce.entity.InterfaceLog;
import com.bruce.service.InterfaceLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * 接口执行时间日志切面（记录UserController所有接口并存储到数据库）
 */
@Aspect
@Component
public class UserControllerTimeLogAspect {

    // 定义ANSI颜色常量（日志控制台彩色输出）
    public static class AnsiColor {
        public static final String RESET = "\033[0m";
        public static final String RED = "\033[31m";
        public static final String GREEN = "\033[32m";
        public static final String YELLOW = "\033[33m";
        public static final String BLUE = "\033[34m";
        public static final String BOLD = "\033[1m";
    }

    private static final Logger log = LoggerFactory.getLogger(UserControllerTimeLogAspect.class);

    // 注入日志服务（MyBatis-Plus实现）
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 切入点：匹配UserController中所有公共方法
     */
    @Pointcut("execution(public * com.bruce.controller.UserController.*(..))")
    public void userControllerPointcut() {}

    /**
     * 环绕通知：记录接口信息并存储到数据库
     */
    @Around("userControllerPointcut()")
    public Object logInterfaceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 生成唯一请求ID
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // 2. 获取请求上下文信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 3. 封装基础日志信息
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setRequestId(requestId);
        interfaceLog.setRequestUrl(request.getRequestURL().toString());
        interfaceLog.setHttpMethod(request.getMethod());
        interfaceLog.setControllerMethod(
                method.getDeclaringClass().getSimpleName() + "." + method.getName()
        );
        interfaceLog.setRequestParams(Arrays.toString(joinPoint.getArgs())); // 参数数组转字符串
        interfaceLog.setStartTime(new Date()); // 改为java.util.Date类型（关键修改）
        interfaceLog.setIpAddress(request.getRemoteAddr()); // 客户端IP

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 4. 执行目标方法
            result = joinPoint.proceed();
            // 5. 记录成功状态
            interfaceLog.setStatus(1); // 1-成功
            interfaceLog.setResponseResult(result != null ? result.toString() : "null");
            return result;
        } catch (Throwable e) {
            // 6. 记录异常状态
            interfaceLog.setStatus(0); // 0-异常
            interfaceLog.setErrorMsg(e.getMessage());
            throw e; // 继续抛出异常，不影响原有业务
        } finally {
            // 7. 计算耗时并保存日志（无论成功失败都执行）
            long costTime = System.currentTimeMillis() - startTime;
            interfaceLog.setCostTime(costTime);
            interfaceLogService.save(interfaceLog); // 调用MyBatis-Plus的save方法入库

            // 8. 控制台彩色日志输出
            if (interfaceLog.getStatus() == 1) {
                log.info(
                        "[" + AnsiColor.BLUE + requestId + AnsiColor.RESET + "] " +
                                "接口执行完成 - 方法: " + AnsiColor.GREEN + interfaceLog.getControllerMethod() + AnsiColor.RESET + ", " +
                                "耗时: " + AnsiColor.YELLOW + costTime + "ms" + AnsiColor.RESET
                );
            } else {
                log.error(
                        "[" + AnsiColor.BLUE + requestId + AnsiColor.RESET + "] " +
                                "接口执行异常 - 方法: " + AnsiColor.RED + interfaceLog.getControllerMethod() + AnsiColor.RESET + ", " +
                                "耗时: " + AnsiColor.YELLOW + costTime + "ms" + AnsiColor.RESET
                );
            }
        }
    }
}
