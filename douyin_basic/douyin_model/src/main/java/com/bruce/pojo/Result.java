package com.bruce.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    private Integer code;    // 业务状态码 0-成功 1-失败
    private String message;  // 提示信息
    private T data;          // 响应数据

    /* ======== Builder ======== */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Integer code;
        private String message;
        private T data;

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Result<T> build() {
            return new Result<>(code, message, data);
        }
    }

    /* ======== 快捷方法 ======== */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    public static Result success() {
        return new Result<>(0, "操作成功", null);
    }

    public static Result error(String message) {
        return new Result<>(1, message, null);
    }
}
