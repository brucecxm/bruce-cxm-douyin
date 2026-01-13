package com.bruce.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常过滤器，用于捕获和处理网关层面的异常
 */
@Component
public class GlobalExceptionFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionFilter.class);
    private final ObjectMapper objectMapper;
    
    public GlobalExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(throwable -> handleException(exchange, throwable));
    }
    
    /**
     * 处理异常
     */
    private Mono<Void> handleException(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        String requestId = request.getId();
        String path = request.getPath().value();
        String method = request.getMethod().name();
        String remoteAddress = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        
        // 记录异常信息
        logger.error("[{}] Exception occurred while processing {} {} from {}", 
                requestId, method, path, remoteAddress, throwable);
        
        // 构建错误响应
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setRequestId(requestId);
        errorResponse.setPath(path);
        errorResponse.setMethod(method);
        
        // 根据异常类型设置状态码和错误信息
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException statusException = (ResponseStatusException) throwable;
            HttpStatus status = statusException.getStatus();
            errorResponse.setCode(status.value());
            errorResponse.setMessage(statusException.getReason() != null ? statusException.getReason() : status.getReasonPhrase());
            response.setStatusCode(status);
        } else {
            // 默认500错误
            errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Gateway Internal Error");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().set("X-Request-Id", requestId);
        
        // 序列化错误响应
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            logger.error("[{}] Failed to serialize error response", requestId, e);
            String errorJson = String.format("{\"requestId\":\"%s\",\"code\":500,\"message\":\"Failed to process error\"}", requestId);
            DataBuffer buffer = response.bufferFactory().wrap(errorJson.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    }
    
    /**
     * 错误响应实体
     */
    static class ErrorResponse {
        private String requestId;
        private String path;
        private String method;
        private int code;
        private String message;
        
        // Getters and setters
        public String getRequestId() {
            return requestId;
        }
        
        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
        
        public String getPath() {
            return path;
        }
        
        public void setPath(String path) {
            this.path = path;
        }
        
        public String getMethod() {
            return method;
        }
        
        public void setMethod(String method) {
            this.method = method;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    @Override
    public int getOrder() {
        // 设置为最高优先级，确保异常能被捕获
        return Ordered.HIGHEST_PRECEDENCE;
    }
}