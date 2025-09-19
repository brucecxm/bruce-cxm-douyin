package com.bruce.service;

import com.bruce.entity.Friend;
import com.bruce.pojo.FriendRequestVO;
import com.bruce.pojo.FriendVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FriendService的测试类，用于验证好友服务的核心功能
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FriendServiceTest {

    @Autowired
    private FriendService friendService;

    /**
     * 测试获取好友列表功能
     * 验证修复后的Long转int类型转换不会导致异常
     */
    @Test
    public void testGetFriendList() {
        // 测试场景1: 调用getFriendList方法
        try {
            List<FriendVO> friendList = friendService.getFriendList();
            assertNotNull(friendList, "好友列表不应为null");
            // 验证方法不会抛出类型转换相关的异常
        } catch (Exception e) {
            fail("获取好友列表时不应抛出异常: " + e.getMessage());
        }
    }

    /**
     * 测试获取好友请求列表功能
     */
    @Test
    public void testGetReceivedRequests() {
        // 测试场景1: 调用getReceivedRequests方法
        try {
            List<FriendRequestVO> requests = friendService.getReceivedRequests();
            assertNotNull(requests, "好友请求列表不应为null");
            // 验证方法不会抛出异常
        } catch (Exception e) {
            fail("获取好友请求列表时不应抛出异常: " + e.getMessage());
        }
    }

    /**
     * 测试处理好友请求功能
     */
    @Test
    public void testHandleRequest() {
        // 测试场景: 调用方法但不验证具体结果，仅验证方法不会抛出异常
        Long requestId = 1L;
        String action = "accept";
        
        try {
            friendService.handleRequest(requestId, action);
            // 方法调用成功，不抛出异常即通过测试
            assertTrue(true);
        } catch (Exception e) {
            fail("处理好友请求时不应抛出异常: " + e.getMessage());
        }
    }

    /**
     * 测试发送好友请求功能
     */
    @Test
    public void testSendFriendRequest() {
        // 测试场景: 调用方法但不验证具体结果，仅验证方法不会抛出异常
        Long toUserId = 2L;
        String verifyMsg = "你好，我想添加你为好友";
        
        try {
            friendService.sendFriendRequest(toUserId, verifyMsg);
            // 方法调用成功，不抛出异常即通过测试
            assertTrue(true);
        } catch (Exception e) {
            fail("发送好友请求时不应抛出异常: " + e.getMessage());
        }
    }
}