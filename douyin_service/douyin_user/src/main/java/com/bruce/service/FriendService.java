package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.dto.FriendRequestVO;
import com.bruce.dto.FriendVO;
import com.bruce.entity.Friend;

import java.util.List;

/**
 * (Friend)表服务接口
 *
 * @author makejava
 * @since 2024-11-10 20:23:23
 */
public interface FriendService extends IService<Friend> {

        void sendFriendRequest(Long toUserId, String verifyMsg);

        List<FriendRequestVO> getReceivedRequests();

        void handleRequest(Long requestId, String action);

        List<FriendVO> getFriendList();

        void deleteFriend(Long friendId);

}

