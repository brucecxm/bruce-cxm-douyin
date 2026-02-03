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
    
    /**
     * 获取用户的粉丝数量
     * @param userId 用户ID
     * @return 粉丝数量
     */
    Integer getFansCount(Long userId);
    
    /**
     * 获取用户的关注数量
     * @param userId 用户ID
     * @return 关注数量
     */
    Integer getFollowCount(Long userId);
    
    /**
     * 获取用户的互关数量
     * @param userId 用户ID
     * @return 互关数量
     */
    Integer getMutualFollowCount(Long userId);

}

