package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bruce.entity.User;
import com.bruce.entity.UserBlacklist;
import com.bruce.entity.UserRelation;
import com.bruce.pojo.Result;
import com.bruce.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/relation")
public class UserRelationController {

    @Autowired
    private UserRelationService relationService;

    @PostMapping("/follow")
    public Result<String> follow(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong(); // 假设你用的是 sa-token
        relationService.follow(userId, targetUserId);
        return Result.success("已关注");
    }

    @DeleteMapping("/unfollow")
    public Result<String> unfollow(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.unfollow(userId, targetUserId);
        return Result.success("已取消关注");
    }

    @PostMapping("/addBestFriend")
    public Result<String> addBestFriend(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.addBestFriend(userId, targetUserId);
        return Result.success("已添加为密友");
    }

    @PostMapping("/block")
    public Result<String> block(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.block(userId, targetUserId);
        return Result.success("已拉黑");
    }

    @PostMapping("/unblock")
    public Result<String> unblock(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.unblock(userId, targetUserId);
        return Result.success("已取消拉黑");
    }

    @GetMapping("/canChat")
    public Result<Boolean> canChat(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.canSendMessage(userId, targetUserId));
    }

    // ✅ 获取我关注的人列表（单向或互关）
    @GetMapping("/myFollowList")
    public Result<List<UserRelation>> myFollowList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getFollowList(userId));
    }

    // ✅ 获取我关注的人列表（单向或互关）
    @GetMapping("/myFollowUserList")
    public Result<List<User>> myFollowUserList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getFollowUserDetails(userId));
    }




    // ✅ 获取我的粉丝列表（别人关注我）
    @GetMapping("/myFansList")
    public Result<List<UserRelation>> myFansList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getFansList(userId));
    }

    // ✅ 获取我的粉丝列表（别人关注我）
    @GetMapping("/myFansUserList")
    public Result<List<User>> myFansUserList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getFansUserDetails(userId));
    }

    // ✅ 获取我和别人互相关注的好友
    @GetMapping("/myFriends")
    public Result<List<UserRelation>> myFriends() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getMutualFollowList(userId));
    }

    // ✅ 获取我和别人互相关注的好友
    @GetMapping("/myFriendsUserInfo")
    public Result<List<User>> myFriendsUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getMutualFollowUserDetails(userId));
    }

    // ✅ 获取密友（单向或双向）
    @GetMapping("/myBestFriends")
    public Result<List<UserRelation>> myBestFriends() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getBestFriends(userId));
    }

    // ✅ 获取拉黑列表
    @GetMapping("/myBlacklist")
    public Result<List<UserBlacklist>> myBlacklist() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getBlacklist(userId));
    }

    // ✅ 查看我和某用户的关系状态
    @GetMapping("/relationWith")
    public Result<String> relationWith(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(relationService.getRelationType(userId, targetUserId));
    }

    // 获取用户关系详细状态
    @GetMapping("/status")
    public Result<Map<String, Boolean>> getRelationStatus(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        Map<String, Boolean> status = new HashMap<>();
        // 检查是否关注对方
        //        status.put("followed", relationService.isFollowing(userId, targetUserId));
        // 检查对方是否关注自己
        //        status.put("fan", relationService.isFollowing(targetUserId, userId));
        // 检查是否互相关注
        status.put("mutual", status.get("followed") && status.get("fan"));
        // 检查是否拉黑
        //        status.put("blocked", relationService.isBlocked(userId, targetUserId));
        // 检查是否密友
        //        status.put("bestFriend", relationService.isBestFriend(userId, targetUserId));
        return Result.success(status);
    }

}
