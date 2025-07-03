package com.bruce.handlar;
// MyCustomException.java

public class MyCustomException extends RuntimeException {
    private int code;

    public MyCustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
