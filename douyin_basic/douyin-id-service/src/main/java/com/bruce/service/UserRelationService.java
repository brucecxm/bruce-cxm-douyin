package com.bruce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bruce.entity.User;
import com.bruce.entity.UserBlacklist;
import com.bruce.entity.UserRelation;

import java.util.List;

/**
 * 用户关系表(UserRelation)表服务接口
 *
 * @author makejava
 * @since 2025-07-29 16:59:40
 */
public interface UserRelationService extends IService<UserRelation> {
    void follow(Long userId, Long targetUserId);

    void unfollow(Long userId, Long targetUserId);

    void addBestFriend(Long userId, Long targetUserId);

    void block(Long userId, Long targetUserId);

    void unblock(Long userId, Long targetUserId);

    boolean canSendMessage(Long userId, Long targetUserId);

    List<UserRelation> getFollowList(Long userId);

    List<User> getFollowUserDetails(Long userId);

    List<UserRelation> getFansList(Long userId);

    List<User> getFansUserDetails(Long userId);

    List<UserRelation> getMutualFollowList(Long userId);

    List<User> getMutualFollowUserDetails(Long userId);

    List<UserRelation> getBestFriends(Long userId);

    List<UserBlacklist> getBlacklist(Long userId);

    String getRelationType(Long userId, Long targetUserId);

}

