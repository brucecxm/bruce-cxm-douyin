package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.entity.User;
import com.bruce.entity.UserBlacklist;
import com.bruce.entity.UserRelation;
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
    public R<String> follow(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong(); // 假设你用的是 sa-token
        relationService.follow(userId, targetUserId);
        return R.ok("已关注");
    }

    @DeleteMapping("/unfollow")
    public R<String> unfollow(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.unfollow(userId, targetUserId);
        return R.ok("已取消关注");
    }

    @PostMapping("/addBestFriend")
    public R<String> addBestFriend(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.addBestFriend(userId, targetUserId);
        return R.ok("已添加为密友");
    }

    @PostMapping("/block")
    public R<String> block(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.block(userId, targetUserId);
        return R.ok("已拉黑");
    }

    @PostMapping("/unblock")
    public R<String> unblock(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        relationService.unblock(userId, targetUserId);
        return R.ok("已取消拉黑");
    }

    @GetMapping("/canChat")
    public R<Boolean> canChat(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.canSendMessage(userId, targetUserId));
    }

    // ✅ 获取我关注的人列表（单向或互关）
    @GetMapping("/myFollowList")
    public R<List<UserRelation>> myFollowList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getFollowList(userId));
    }

    // ✅ 获取我关注的人列表（单向或互关）
    @GetMapping("/myFollowUserList")
    public R<List<User>> myFollowUserList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getFollowUserDetails(userId));
    }



    // ✅ 获取我的粉丝列表（别人关注我）
    @GetMapping("/myFansList")
    public R<List<UserRelation>> myFansList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getFansList(userId));
    }

    // ✅ 获取我的粉丝列表（别人关注我）
    @GetMapping("/myFansUserList")
    public R<List<User>> myFansUserList() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getFansUserDetails(userId));
    }

    // ✅ 获取我和别人互相关注的好友
    @GetMapping("/myFriends")
    public R<List<UserRelation>> myFriends() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getMutualFollowList(userId));
    }

    // ✅ 获取我和别人互相关注的好友
    @GetMapping("/myFriendsUserInfo")
    public R<List<User>> myFriendsUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getMutualFollowUserDetails(userId));
    }

    // ✅ 获取密友（单向或双向）
    @GetMapping("/myBestFriends")
    public R<List<UserRelation>> myBestFriends() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getBestFriends(userId));
    }

    // ✅ 获取拉黑列表
    @GetMapping("/myBlacklist")
    public R<List<UserBlacklist>> myBlacklist() {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getBlacklist(userId));
    }

    // ✅ 查看我和某用户的关系状态
    @GetMapping("/relationWith")
    public R<String> relationWith(@RequestParam Long targetUserId) {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(relationService.getRelationType(userId, targetUserId));
    }

    // 获取用户关系详细状态
    @GetMapping("/status")
    public R<Map<String, Boolean>> getRelationStatus(@RequestParam Long targetUserId) {
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
        return R.ok(status);
    }

}
