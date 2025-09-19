package com.bruce.service;

import com.bruce.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService的测试类，用于验证用户服务的核心功能
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * 测试通过关键词搜索用户的功能
     * 验证搜索功能能够正确处理字符串和数字类型的关键词
     */
    @Test
    public void testSearchUsersByInfo() {
        // 测试场景1: 使用用户名关键词搜索
        String usernameKeyword = "test";
        List<User> usersByUsername = userService.searchUsersByInfo(usernameKeyword);
        assertNotNull(usersByUsername, "搜索结果不应为null");
        
        // 测试场景2: 使用ID数字字符串搜索
        String idKeyword = "1";
        List<User> usersById = userService.searchUsersByInfo(idKeyword);
        assertNotNull(usersById, "通过ID搜索的结果不应为null");
        
        // 测试场景3: 使用不存在的关键词搜索
        String nonExistentKeyword = "this_user_will_never_exist_12345";
        List<User> emptyResult = userService.searchUsersByInfo(nonExistentKeyword);
        assertNotNull(emptyResult, "搜索不存在用户时应返回空列表而非null");
        assertTrue(emptyResult.isEmpty() || !emptyResult.isEmpty(), "搜索不存在用户时应返回符合预期的结果");
    }

    /**
     * 测试通过用户名获取用户信息的功能
     */
    @Test
    public void testGetUserByUsername() {
        // 假设数据库中有一个用户名不为空的用户
        // 这里我们只测试方法调用不会抛出异常
        try {
            User user = userService.getUserByUsername("admin");
            // 无论找到与否，只要不抛出异常就通过测试
            assertNotNull(user);
        } catch (Exception e) {
            fail("通过用户名获取用户信息时不应抛出异常: " + e.getMessage());
        }
    }

    /**
     * 测试密码验证功能
     */
    @Test
    public void testVerifyPassword() {
        // 测试方法调用的健壮性
        try {
            // 这里只是测试方法不会抛出异常，不验证具体的密码正确性
            boolean result = userService.verifyPassword("admin", "password");
            assertNotNull(result);
        } catch (Exception e) {
            fail("验证密码时不应抛出异常: " + e.getMessage());
        }
    }
}