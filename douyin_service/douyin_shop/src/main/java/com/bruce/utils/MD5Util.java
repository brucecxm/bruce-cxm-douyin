package com.bruce.utils;
import org.springframework.util.DigestUtils;

public class MD5Util {

    public static String generateMD5(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes());
    }
}
