package com.bruce.utils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

    // 生成UUID
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
