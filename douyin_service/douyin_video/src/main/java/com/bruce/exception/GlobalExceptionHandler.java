package com.bruce.exception;

import com.bruce.entity.Result;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AmqpConnectException.class)
    public ResponseEntity<String> handleAmqpConnectException(AmqpConnectException e) {
        // 记录日志或执行其他处理逻辑
        System.err.println("RabbitMQ connection failed: " + e.getMessage());

        // 返回自定义的错误响应
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("RabbitMQ connection error: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        // 打印异常堆栈
        e.printStackTrace();

        // 返回自定义的错误响应
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}
