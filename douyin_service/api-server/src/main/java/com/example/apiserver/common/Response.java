package com.example.apiserver.common;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response<>(0, "success", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(0, "success", data);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(0, message, data);
    }

    public static <T> Response<T> error() {
        return new Response<>(1, "error", null);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(1, message, null);
    }

    public static <T> Response<T> error(Integer code, String message) {
        return new Response<>(code, message, null);
    }
}