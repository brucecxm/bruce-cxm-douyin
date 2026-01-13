package com.bruce.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 全局日志过滤器，用于记录所有请求和响应信息
 */
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalLoggingFilter.class);
    private static final Logger accessLogger = LoggerFactory.getLogger("gateway-access");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        String requestId = request.getId();
        URI uri = request.getURI();
        HttpMethod method = request.getMethod();
        String path = uri.getPath();
        String remoteAddress = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        // 记录请求信息
        logger.info("[{}] Request received: {} {} from {}", requestId, method, path, remoteAddress);
        logger.debug("[{}] Request headers: {}", requestId, request.getHeaders());

        // 继续处理请求
        return chain.filter(exchange).doFinally(signalType -> {
            // 记录响应信息
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            HttpStatus status = response.getStatusCode();
            int statusCode = status != null ? status.value() : 0;

            // 访问日志格式：时间戳 请求方法 请求路径 状态码 响应时间 客户端IP
            accessLogger.info("{} {} {} {} {}ms {}",
                    endTime, method, path, statusCode, duration, remoteAddress);

            // 根据状态码记录不同级别的日志
            if (statusCode >= 500) {
                logger.error("[{}] Request failed: {} {} -> {} in {}ms",
                        requestId, method, path, statusCode, duration);
                logger.error("[{}] Response headers: {}", requestId, response.getHeaders());
            } else if (statusCode >= 400) {
                logger.warn("[{}] Request completed with warning: {} {} -> {} in {}ms",
                        requestId, method, path, statusCode, duration);
            } else {
                logger.info("[{}] Request completed successfully: {} {} -> {} in {}ms",
                        requestId, method, path, statusCode, duration);
            }
        });
}

@Override
    public int getOrder() {
        // 设置过滤器优先级，数值越小优先级越高
        return Ordered.HIGHEST_PRECEDENCE;
    }
}