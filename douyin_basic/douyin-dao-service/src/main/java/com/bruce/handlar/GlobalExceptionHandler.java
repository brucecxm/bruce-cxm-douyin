package com.bruce.handlar;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 统一返回 JSON
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        e.printStackTrace(); // 开发阶段可打印
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", e.getMessage());
        return result;
    }

    @ExceptionHandler(MyCustomException.class)
    public Map<String, Object> handleMyCustomException(MyCustomException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", e.getCode());
        result.put("msg", e.getMessage());
        return result;
    }

    @ExceptionHandler(ClientAbortException.class)
    public Map<String, Object> handleClientAbortException(ClientAbortException e) {
        // ClientAbortException是客户端主动断开连接导致的异常，不影响服务稳定性，无需详细日志
        Map<String, Object> result = new HashMap<>();
        result.put("code", 499);
        result.put("msg", "Client closed connection");
        return result;
    }
}
