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
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 通用接口日志切面（支持HTTP、WebSocket等多种类型请求，兼容各种场景）
 */
@Aspect
@Component
public class UserControllerTimeLogAspect {

    // 日志颜色常量（兼容不支持ANSI的环境）
    public static class AnsiColor {
        public static final String RESET = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[0m";
        public static final String RED = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[31m";
        public static final String GREEN = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[32m";
        public static final String YELLOW = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[33m";
        public static final String BLUE = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[34m";
        public static final String BOLD = System.getProperty("os.name").toLowerCase().contains("windows") ? "" : "\033[1m";
    }

    private static final Logger log = LoggerFactory.getLogger(UserControllerTimeLogAspect.class);

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 切入点：匹配controller包下所有类的公共方法（支持各种请求类型）
     */
    @Pointcut("execution(public * com.bruce.controller..*(..))")
    public void controllerPointcut() {}

    /**
     * 环绕通知：增强兼容性，支持多种请求类型
     */
    @Around("controllerPointcut()")
    public Object logInterfaceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 生成唯一请求ID（追踪全流程）
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setRequestId(requestId);
        interfaceLog.setStartTime(new Date());

        try {
            // 2. 解析请求上下文（兼容非HTTP场景）
            parseRequestContext(joinPoint, interfaceLog);

            // 3. 执行目标方法
            Object result = joinPoint.proceed();

            // 4. 处理响应结果
            handleSuccessResult(interfaceLog, result);
            return result;
        } catch (Throwable e) {
            // 5. 处理异常信息
            handleException(interfaceLog, e);
            throw e; // 不中断原有异常流程
        } finally {
            // 6. 计算耗时并保存日志（最终执行）
            long costTime = System.currentTimeMillis() - startTime;
            interfaceLog.setCostTime(costTime);
            saveInterfaceLog(interfaceLog);

            // 7. 控制台日志输出（区分成功/异常）
            printConsoleLog(interfaceLog, costTime);
        }
    }

    /**
     * 解析请求上下文信息（兼容HTTP和非HTTP场景）
     */
    private void parseRequestContext(ProceedingJoinPoint joinPoint, InterfaceLog interfaceLog) {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        interfaceLog.setControllerMethod(method.getDeclaringClass().getSimpleName() + "." + method.getName());

        // 解析请求参数（脱敏处理敏感信息）
        Object[] args = joinPoint.getArgs();
        interfaceLog.setRequestParams(desensitizeParams(args));

        // 区分HTTP请求和WebSocket请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getRequest() != null) {
            // HTTP请求处理
            HttpServletRequest request = attributes.getRequest();
            interfaceLog.setRequestUrl(request.getRequestURL().toString());
            interfaceLog.setHttpMethod(request.getMethod());
            interfaceLog.setIpAddress(getClientIp(request));
        } else if (isWebSocketRequest(args)) {
            // WebSocket请求处理
            interfaceLog.setRequestUrl("ws://" + getLocalIp() + ":" + getLocalPort() + "/" + interfaceLog.getControllerMethod());
            interfaceLog.setHttpMethod("WS");
            interfaceLog.setIpAddress(getLocalIp()); // WebSocket取本地IP
        } else {
            // 其他类型请求（如内部调用）
            interfaceLog.setRequestUrl("internal://" + interfaceLog.getControllerMethod());
            interfaceLog.setHttpMethod("N/A");
            interfaceLog.setIpAddress(getLocalIp());
        }
    }

    /**
     * 处理成功响应结果
     */
    private void handleSuccessResult(InterfaceLog interfaceLog, Object result) {
        interfaceLog.setStatus(1); // 1-成功
        if (result != null) {
            // 限制结果长度，避免大对象占用过多存储
            String resultStr = result.toString();
            interfaceLog.setResponseResult(resultStr.length() > 2000 ? resultStr.substring(0, 2000) + "..." : resultStr);
        } else {
            interfaceLog.setResponseResult("null");
        }
    }

    /**
     * 处理异常信息
     */
    private void handleException(InterfaceLog interfaceLog, Throwable e) {
        interfaceLog.setStatus(0); // 0-异常
        interfaceLog.setErrorMsg(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        // 记录异常堆栈（可选，按需开启）
        // interfaceLog.setErrorStack(Arrays.toString(e.getStackTrace()).substring(0, 2000));
    }

    /**
     * 保存日志到数据库（兼容异步场景）
     */
    private void saveInterfaceLog(InterfaceLog interfaceLog) {
        try {
            interfaceLogService.save(interfaceLog);
        } catch (Exception e) {
            // 日志保存失败不影响主流程，仅记录警告
            this.log.warn("接口日志保存失败: {}", e.getMessage());
        }
    }

    /**
     * 控制台日志输出（彩色区分）
     */
    private void printConsoleLog(InterfaceLog interfaceLog, long costTime) {
        // 根据httpMethod推断请求类型
        String requestType = "HTTP";
        if ("WS".equals(interfaceLog.getHttpMethod())) {
            requestType = "WebSocket";
        } else if ("N/A".equals(interfaceLog.getHttpMethod())) {
            requestType = "INTERNAL";
        }
        
        String baseLog = "[" + AnsiColor.BLUE + interfaceLog.getRequestId() + AnsiColor.RESET + "] " +
                "类型: " + requestType + ", " +
                "方法: " + AnsiColor.BOLD + interfaceLog.getControllerMethod() + AnsiColor.RESET + ", " +
                "耗时: " + AnsiColor.YELLOW + costTime + "ms" + AnsiColor.RESET;

        if (interfaceLog.getStatus() == 1) {
            this.log.info(baseLog + ", 状态: " + AnsiColor.GREEN + "成功" + AnsiColor.RESET);
        } else {
            this.log.error(baseLog + ", 状态: " + AnsiColor.RED + "异常" + AnsiColor.RESET + ", 原因: " + interfaceLog.getErrorMsg());
        }
    }

    /**
     * 判断是否为WebSocket请求（参数包含WebSocketSession）
     */
    private boolean isWebSocketRequest(Object[] args) {
        if (args == null) return false;
        for (Object arg : args) {
            if (arg instanceof WebSocketSession) {
                return true;
            }
        }
        return false;
    }

    /**
     * 参数脱敏处理（过滤密码等敏感信息）
     */
    private String desensitizeParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        List<Object> safeArgs = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof Map) {
                // 处理Map类型参数（如@RequestParam的Map）
                Map<?, ?> map = (Map<?, ?>) arg;
                Map<String, Object> safeMap = new HashMap<>();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();
                    // 脱敏密码、token等敏感字段
                    if (key.contains("password") || key.contains("token") || key.contains("secret")) {
                        safeMap.put(key, "***");
                    } else {
                        safeMap.put(key, value);
                    }
                }
                safeArgs.add(safeMap);
            } else {
                // 非Map参数直接添加（如需进一步处理实体类，可在这里扩展）
                safeArgs.add(arg);
            }
        }
        return Arrays.toString(safeArgs.toArray());
    }

    /**
     * 获取客户端真实IP（兼容代理场景）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多代理情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取本地IP地址
     */
    private String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    /**
     * 获取本地端口（默认8080，可根据实际情况调整）
     */
    private int getLocalPort() {
        // 实际项目中可从环境变量或配置中获取
        return 7456; // 你的应用端口
    }
}
