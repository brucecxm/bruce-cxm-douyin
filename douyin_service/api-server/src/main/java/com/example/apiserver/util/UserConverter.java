package com.example.apiserver.util;

import com.example.apiserver.entity.User;

import java.util.Date;
import java.util.Map;

public class UserConverter {

    /**
     * 将 Map 转换为 User 对象
     *
     * @param userMap 包含用户信息的 Map
     * @return User 对象
     */
    public static User convertToUser(Map<String, Object> userMap) {
        if (userMap == null || userMap.isEmpty()) {
            return null;
        }
        User user = new User();
        Object idValue = userMap.get("id");
        if (idValue != null) {
            user.setId(convertToLong(idValue));
        }
        Object usernameValue = userMap.get("username");
        if (usernameValue != null) {
            user.setUsername((String) usernameValue);
        }
        Object passwordValue = userMap.get("password");
        if (passwordValue != null) {
            user.setPassword((String) passwordValue);
        }
        Object nicknameValue = userMap.get("nickname");
        if (nicknameValue != null) {
            user.setNickname((String) nicknameValue);
        }
        Object emailValue = userMap.get("email");
        if (emailValue != null) {
            user.setEmail((String) emailValue);
        }
        Object phoneValue = userMap.get("phone");
        if (phoneValue != null) {
            user.setPhone((String) phoneValue);
        }
        Object avatarValue = userMap.get("avatar");
        if (avatarValue != null) {
            user.setAvatar((String) avatarValue);
        }
        Object genderValue = userMap.get("gender");
        if (genderValue != null) {
            user.setGender((String) genderValue);
        }
        Object birthdayValue = userMap.get("birthday");
        if (birthdayValue != null) {
            user.setBirthday(convertToDate(birthdayValue));
        }
        Object bioValue = userMap.get("bio");
        if (bioValue != null) {
            user.setBio((String) bioValue);
        }
        Object createdAtValue = userMap.get("createdAt");
        if (createdAtValue != null) {
            user.setCreatedAt(convertToDate(createdAtValue));
        }
        Object updatedAtValue = userMap.get("updatedAt");
        if (updatedAtValue != null) {
            user.setUpdatedAt(convertToDate(updatedAtValue));
        }
        return user;
    }

    /**
     * 将 Object 转换为 Long
     *
     * @param value 要转换的对象
     * @return Long 值
     */
    private static Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        throw new IllegalArgumentException("无法将 " + value.getClass() + " 转换为 Long");
    }

    /**
     * 将 Object 转换为 Date
     *
     * @param value 要转换的对象
     * @return Date 值
     */
    private static Date convertToDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return new Date(Long.parseLong((String) value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + value + " 转换为 Date");
            }
        }
        throw new IllegalArgumentException("无法将 " + value.getClass() + " 转换为 Date");
    }
}
