package com.bruce.utils;

import com.alibaba.fastjson.JSON;

/**
 * 封装fastjson的方法，若需要换Json工具，也不需要到处改
 * @author Hai
 * @date 2020/10/2 - 22:06
 */
public class JsonUtil extends JSON {


    public static String parseToString(Object object) {

        return toJSONString(object);

    }

    public static <T> T parseToObject(String text, Class<T> clazz) {
        return parseObject(text, clazz);
    }

}
