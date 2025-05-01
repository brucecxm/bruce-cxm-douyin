package com.bruce.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 通用统一返回对象
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 752386055477259305L;

    public static final String DEFAULT_SUCCESS_MSG = "操作成功";
    public static final String DEFAULT_ERROR_MSG = "操作失败";
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = 1;

    private Integer code;    // 状态码：0-成功，1-失败，或自定义
    private String message;  // 提示信息
    private T data;          // 响应数据
    private boolean result;  // 兼容旧版字段（可选）

    // --------------------- 快捷构造方法 ---------------------

    public static <E> Result<E> success(E data) {
        return new Result<>(CODE_SUCCESS, DEFAULT_SUCCESS_MSG, data, true);
    }

    public static <E> Result<E> success(String message, E data) {
        return new Result<>(CODE_SUCCESS, message, data, true);
    }

    public static Result<Void> success() {
        return new Result<>(CODE_SUCCESS, DEFAULT_SUCCESS_MSG, null, true);
    }

    public static Result<Void> error(String message) {
        return new Result<>(CODE_ERROR, message, null, false);
    }

    // 兼容旧版本构造
    public Result(Integer code, String message, T data, boolean result) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.result = result;
    }
}
