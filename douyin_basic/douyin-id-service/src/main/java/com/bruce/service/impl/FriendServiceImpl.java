package com.bruce.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.FriendDao;
import com.bruce.dao.FriendRequestDao;
import com.bruce.dao.UserDao;
import com.bruce.pojo.FriendRequestVO;
import com.bruce.pojo.FriendVO;
import com.bruce.entity.Friend;
import com.bruce.entity.FriendRequest;
import com.bruce.entity.User;
import com.bruce.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (Friend)表服务实现类
 *
 * @author makejava
 * @since 2024-11-10 20:23:23
 */
@Service("friendService")
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements FriendService {
    @Autowired
    private FriendRequestDao friendRequestMapper;

    @Autowired
    private FriendDao friendMapper;

    @Autowired
    private UserDao userMapper;



    /**
     * 发送好友请求
     */
    @Override
    public void sendFriendRequest(Long toUserId, String verifyMsg) {
        Object fromUserIdObj = StpUtil.getLoginId();
Long fromUserId= 0L;
// 判断返回值是否为 Long 类型
        if (fromUserIdObj != null) {
             fromUserId = Long.valueOf(fromUserIdObj.toString());
            // 现在 fromUserId 就是 Long 类型
        }


        // 判断是否已经是好友
        int count = friendMapper.selectCount(new LambdaQueryWrapper<Friend>()
                .eq(Friend::getUserId, fromUserId)  // 查找当前用户与目标用户之间的好友关系
                .eq(Friend::getFriendId, toUserId));
        if (count > 0) { // 如果已是好友
            throw new RuntimeException("你们已经是好友了"); // 抛出异常
        }
        // 写入好友请求记录
        FriendRequest request = new FriendRequest();
        request.setFromUserId(fromUserId); // 设置请求发起人ID
        request.setToUserId(toUserId);     // 设置请求接收人ID
        request.setVerifyMsg(verifyMsg);   // 设置验证消息
        request.setStatus(0);              // 设置请求状态：0 = 待处理
        request.setCreateTime(LocalDateTime.now()); // 设置请求创建时间
        friendRequestMapper.insert(request);
    }

    /**
     * 查看收到的好友请求
     */
    @Override
    public List<FriendRequestVO> getReceivedRequests() {
        Object myIdStr = StpUtil.getLoginId();
        Long myId= 0L;
// 判断返回值是否为 Long 类型
        if (myIdStr != null) {
            myId = Long.valueOf(myIdStr.toString());
            // 现在 fromUserId 就是 Long 类型
        }
        // 获取当前用户收到的好友请求，并按请求时间倒序排序
        List<FriendRequest> requests = friendRequestMapper.selectList(
                new LambdaQueryWrapper<FriendRequest>()
                        .eq(FriendRequest::getToUserId, myId)  // 查找接收人是当前用户的请求
                        .orderByDesc(FriendRequest::getCreateTime) // 按请求时间倒序
        );

        List<FriendRequestVO> voList = new ArrayList<>();
        for (FriendRequest req : requests) {
            // 获取请求发起人的用户信息
            User fromUser = userMapper.selectById(req.getFromUserId());
            FriendRequestVO vo = new FriendRequestVO();
            vo.setRequestId(req.getId()); // 设置请求ID
            vo.setFromUserId((int) fromUser.getId()); // 设置发起人ID
            vo.setNickname(fromUser.getNickname()); // 设置发起人昵称
            vo.setAvatar(fromUser.getAvatar()); // 设置发起人头像
            vo.setVerifyMsg(req.getVerifyMsg()); // 设置验证消息
            vo.setStatus(getStatusName(req.getStatus())); // 设置请求状态
            vo.setRequestTime(req.getCreateTime()); // 设置请求时间
            voList.add(vo); // 将结果添加到列表
        }
        return voList; // 返回收到的好友请求列表
    }

    /**
     * 处理好友请求
     */
    @Override
    public void handleRequest(Long requestId, String action) {
        Object myIdStr = StpUtil.getLoginId();
        Long myId= 0L;

        if (myIdStr != null) {
            myId = Long.valueOf(myIdStr.toString());
            // 现在 fromUserId 就是 Long 类型
        }
        // 获取请求记录
        FriendRequest request = friendRequestMapper.selectById(requestId);
        if (request == null || !request.getToUserId().equals(myId)) {
            throw new RuntimeException("非法操作"); // 请求不存在或不是当前用户的请求
        }
        if (request.getStatus() != 0) {
            throw new RuntimeException("该请求已处理"); // 如果请求已经被处理过，抛出异常
        }

        // 根据操作类型处理请求
        if ("accept".equalsIgnoreCase(action)) {
            // 接受请求
            request.setStatus(1); // 设置请求状态为已同意
            friendRequestMapper.updateById(request); // 更新请求状态

            // 添加好友关系（双向）
            insertFriendRelation(myId, request.getFromUserId()); // 当前用户与发起人建立好友关系
            insertFriendRelation(request.getFromUserId(), myId); // 发起人和当前用户建立好友关系
        } else if ("reject".equalsIgnoreCase(action)) {
            // 拒绝请求
            request.setStatus(2); // 设置请求状态为已拒绝
            friendRequestMapper.updateById(request); // 更新请求状态
        } else {
            throw new RuntimeException("无效操作类型"); // 如果操作类型不合法，抛出异常
        }
    }

    /**
     * 获取好友列表
     */
    @Override
    public List<FriendVO> getFriendList() {
        Object myIdStr = StpUtil.getLoginId();
        Long myId= 0L;

        if (myIdStr != null) {
            myId = Long.valueOf(myIdStr.toString());
            // 现在 fromUserId 就是 Long 类型
        }
        // 获取当前用户的好友列表，状态为已同意的
        List<Friend> friends = friendMapper.selectList(
                new LambdaQueryWrapper<Friend>()
                        .eq(Friend::getUserId, myId)  // 查找当前用户的好友
                        .eq(Friend::getStatus, 1)    // 好友状态为已同意
        );

        List<FriendVO> voList = new ArrayList<>();
        for (Friend f : friends) {
            // 获取好友的用户信息
            User u = userMapper.selectById(f.getFriendId());
            FriendVO vo = new FriendVO();
            vo.setUserId((int) u.getId()); // 设置好友ID
            vo.setNickname(u.getNickname()); // 设置好友昵称
            vo.setAvatar(u.getAvatar()); // 设置好友头像
            vo.setGender(u.getGender()); // 设置好友性别
            vo.setRemark(f.getRemark()); // 设置备注
            vo.setAddedTime(f.getCreatedTime()); // 设置好友添加时间
            voList.add(vo); // 将结果添加到列表
        }
        return voList; // 返回好友列表
    }

    /**
     * 删除好友（双向删除）
     */
    @Override
    public void deleteFriend(Long friendId) {
        Object myId = StpUtil.getLoginId();
        // 删除自己与对方的好友关系
        friendMapper.delete(new LambdaQueryWrapper<Friend>()
                .eq(Friend::getUserId, myId)   // 查找当前用户与指定好友的关系
                .eq(Friend::getFriendId, friendId));
        // 删除对方与自己的好友关系
        friendMapper.delete(new LambdaQueryWrapper<Friend>()
                .eq(Friend::getUserId, friendId) // 查找指定好友与当前用户的关系
                .eq(Friend::getFriendId, myId));
    }

    /**
     * 插入好友关系
     */
    private void insertFriendRelation(Long userId, Long friendId) {
        Friend friend = new Friend();
        friend.setUserId(userId); // 设置当前用户ID
        friend.setFriendId(friendId); // 设置好友ID
        friend.setStatus(1); // 设置状态为已同意
        friend.setCreatedTime(LocalDateTime.now()); // 设置创建时间
        friendMapper.insert(friend); // 插入好友关系记录
    }

    /**
     * 获取请求状态的名称
     */
    private String getStatusName(int status) {
        switch (status) {
            case 0: return "待处理"; // 0：待处理
            case 1: return "已同意"; // 1：已同意
            case 2: return "已拒绝"; // 2：已拒绝
            default: return "未知"; // 未知状态
        }
    }

}

