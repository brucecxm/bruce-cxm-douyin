package com.bruce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bruce.dao.UserBlacklistDao;
import com.bruce.dao.UserRelationDao;
import com.bruce.entity.User;
import com.bruce.entity.UserBlacklist;
import com.bruce.entity.UserRelation;
import com.bruce.service.UserRelationService;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关系表(UserRelation)表服务实现类
 *
 * @author makejava
 * @since 2025-07-29 16:59:40
 */
@Service("userRelationService")
public class UserRelationServiceImpl extends ServiceImpl<UserRelationDao, UserRelation>
        implements UserRelationService {

    @Autowired
    private UserBlacklistDao blacklistMapper;

    @Autowired
    private UserService userService;

    @Override
    public void follow(Long userId, Long targetUserId) {
        // 先判断是否在黑名单中
        if (isBlocked(userId, targetUserId)) return;

        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));

        if (relation == null) {
            relation = new UserRelation();
            relation.setUserId(userId);
            relation.setTargetUserId(targetUserId);
            relation.setRelationType(1); // 单向关注
            save(relation);
        }

        // 检查是否互关
        UserRelation reverse = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, targetUserId)
                .eq(UserRelation::getTargetUserId, userId));

        if (reverse != null && reverse.getRelationType() == 1) {
            relation.setRelationType(2);
            reverse.setRelationType(2);
            updateById(relation);
            updateById(reverse);
        }
    }

    @Override
    public void unfollow(Long userId, Long targetUserId) {
        // 先查询当前用户对目标用户的关注记录
        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));

        if (relation == null) {
            // 没有关注记录，直接返回
            return;
        }

        // 删除当前用户对目标用户的关注记录
        removeById(relation.getId());

        // 查询目标用户对当前用户的关系
        UserRelation reverse = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, targetUserId)
                .eq(UserRelation::getTargetUserId, userId));

        if (reverse != null) {
            // 如果目标用户关注了当前用户，将其关系类型从互关(2)改为单向关注(1)
            if (reverse.getRelationType() == 2) {
                reverse.setRelationType(1);
                updateById(reverse);
            }
        }
    }

    @Override
    public void addBestFriend(Long userId, Long targetUserId) {
        if (isBlocked(userId, targetUserId)) return;

        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));

        if (relation == null) {
            relation = new UserRelation();
            relation.setUserId(userId);
            relation.setTargetUserId(targetUserId);
            relation.setRelationType(3); // 单向密友
            save(relation);
        } else {
            relation.setRelationType(3);
            updateById(relation);
        }

        UserRelation reverse = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, targetUserId)
                .eq(UserRelation::getTargetUserId, userId));

        if (reverse != null && reverse.getRelationType() == 3) {
            relation.setRelationType(4);
            reverse.setRelationType(4);
            updateById(relation);
            updateById(reverse);
        }
    }

    @Override
    public void block(Long userId, Long targetUserId) {
        UserBlacklist exist = blacklistMapper.selectOne(new LambdaQueryWrapper<UserBlacklist>()
                .eq(UserBlacklist::getUserId, userId)
                .eq(UserBlacklist::getTargetUserId, targetUserId));

        if (exist == null) {
            UserBlacklist bl = new UserBlacklist();
            bl.setUserId(userId);
            bl.setTargetUserId(targetUserId);
            blacklistMapper.insert(bl);
        }

        remove(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));

        remove(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, targetUserId)
                .eq(UserRelation::getTargetUserId, userId));
    }

    @Override
    public void unblock(Long userId, Long targetUserId) {
        // 移除黑名单记录
        blacklistMapper.delete(new LambdaQueryWrapper<UserBlacklist>()
                .eq(UserBlacklist::getUserId, userId)
                .eq(UserBlacklist::getTargetUserId, targetUserId));
    }

    @Override
    public boolean canSendMessage(Long userId, Long targetUserId) {
        if (isBlocked(userId, targetUserId)) return false;

        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));
        return relation != null && (relation.getRelationType() == 2 || relation.getRelationType() == 4);
    }

    private boolean isBlocked(Long userId, Long targetUserId) {
        return blacklistMapper.selectCount(new LambdaQueryWrapper<UserBlacklist>()
                .eq(UserBlacklist::getUserId, userId)
                .eq(UserBlacklist::getTargetUserId, targetUserId)) > 0
                || blacklistMapper.selectCount(new LambdaQueryWrapper<UserBlacklist>()
                .eq(UserBlacklist::getUserId, targetUserId)
                .eq(UserBlacklist::getTargetUserId, userId)) > 0;
    }

    @Override
    public List<UserRelation> getFollowList(Long userId) {
        return list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .in(UserRelation::getRelationType, 1, 2, 3, 4));
    }


    /**
     * 获取指定用户关注的所有用户信息
     * @param userId 当前用户ID
     * @return 关注的用户列表
     */
    public List<User> getFollowUserDetails(Long userId) {
        // 1. 先查询当前用户的所有关注关系
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .in(UserRelation::getRelationType, 1, 2, 3, 4));

        // 2. 提取所有被关注用户的ID
        List<Long> targetUserIds = relations.stream()
                .map(UserRelation::getTargetUserId)
                .distinct() // 去重，避免重复关注的情况
                .collect(Collectors.toList());

        if (targetUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 根据目标用户ID列表查询用户详情
        return userService.list(new LambdaQueryWrapper<User>()
                .in(User::getId, targetUserIds));
    }

    @Override
    public List<UserRelation> getFansList(Long userId) {
        return list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getTargetUserId, userId)
                .in(UserRelation::getRelationType, 1, 2, 3, 4));
    }

    @Override
    public List<User> getFansUserDetails(Long userId) {
        // 1. 先查询当前用户的所有关注关系
        List<UserRelation> relations =  list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getTargetUserId, userId)
                .in(UserRelation::getRelationType, 1, 2, 3, 4));
        // 2. 提取所有被关注用户的ID
        List<Long> targetUserIds = relations.stream()
                .map(UserRelation::getTargetUserId)
                .distinct() // 去重，避免重复关注的情况
                .collect(Collectors.toList());

        if (targetUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 根据目标用户ID列表查询用户详情
        return userService.list(new LambdaQueryWrapper<User>()
                .in(User::getId, targetUserIds));
    }

    @Override
    public List<UserRelation> getMutualFollowList(Long userId) {
        List<UserRelation> myFollows = list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getRelationType, 2));
        return myFollows;
    }

    @Override
    public List<User> getMutualFollowUserDetails(Long userId) {


        // 1. 先查询当前用户的所有关注关系
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getRelationType, 2));
        // 2. 提取所有被关注用户的ID
        List<Long> targetUserIds = relations.stream()
                .map(UserRelation::getTargetUserId)
                .distinct() // 去重，避免重复关注的情况
                .collect(Collectors.toList());

        if (targetUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 根据目标用户ID列表查询用户详情
        return userService.list(new LambdaQueryWrapper<User>()
                .in(User::getId, targetUserIds));

    }

    @Override
    public List<UserRelation> getBestFriends(Long userId) {
        return list(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .in(UserRelation::getRelationType, 3, 4));
    }

    @Override
    public List<UserBlacklist> getBlacklist(Long userId) {
        return blacklistMapper.selectList(new LambdaQueryWrapper<UserBlacklist>()
                .eq(UserBlacklist::getUserId, userId));
    }

    @Override
    public String getRelationType(Long userId, Long targetUserId) {
        // 拉黑优先
        boolean blocked = isBlocked(userId, targetUserId);
        if (blocked) return "屏蔽（黑名单）";

        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
                .eq(UserRelation::getUserId, userId)
                .eq(UserRelation::getTargetUserId, targetUserId));

        if (relation == null) return "无关系";

        String relationTypeStr;
        switch (relation.getRelationType()) {
            case 1:
                relationTypeStr = "我关注他";
                break;
            case 2:
                relationTypeStr = "互相关注";
                break;
            case 3:
                relationTypeStr = "我将他设为密友";
                break;
            case 4:
                relationTypeStr = "互为密友";
                break;
            default:
                relationTypeStr = "未知关系";
                break;
        }
        return relationTypeStr;
    }


}


