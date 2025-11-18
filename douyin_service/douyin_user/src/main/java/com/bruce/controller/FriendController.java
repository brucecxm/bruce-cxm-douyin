package com.bruce.controller;



import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.User;
import com.bruce.pojo.FriendRequestDTO;
import com.bruce.pojo.FriendRequestVO;
import com.bruce.pojo.FriendVO;
import com.bruce.pojo.HandleRequestDTO;
import com.bruce.entity.Friend;
import com.bruce.service.FriendService;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (Friend)表控制层
 *
 * @author makejava
 * @since 2024-11-10 20:23:21
 */
@RestController
@RequestMapping("/user/friend")
public class FriendController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private FriendService friendService;




    @Autowired
    private UserService userService;





    /**
     *
     *
     *
     * 分页查询所有数据
     *
     * @param friend 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(
            @RequestParam(defaultValue = "1") int current,  // 当前页，默认为1
            @RequestParam(defaultValue = "10") int size,    // 每页大小，默认为10
            Friend friend) {

        // 创建一个分页对象
        Page<Friend> page = new Page<>(current, size);

        return success(this.friendService.page(page, new QueryWrapper<>(friend)));
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("/all")
    public R selectAllFriends() {
        List<Friend> friends = this.friendService.list();
        return success(friends);
    }
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.friendService.getById(id));
    }

    /**
     * 搜索我的好友列表中的用户（用于我的好友列表搜索）
     */
    @GetMapping("/list/search/zan")
    public ResponseEntity<?> searchMyFriends(@RequestParam String keyword) {
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        List<User> users = friendService.searchMyFriends(userId, keyword);
        return ResponseEntity.ok(users);
    }

    /**
     * 搜索我的好友列表中的用户（用于我的好友列表搜索）
     */
    @GetMapping("/list/search")
    public ResponseEntity<?> searchUser(@RequestParam String keyword) {
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        List<User> users = friendService.searchMyFriends(userId, keyword);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据实体类属性查询所有匹配记录的数量
     *
     * @param friend 实体类对象，包含查询条件
     * @return 匹配的记录数量
     */
    @GetMapping("/count")
    public R countByAttributes(Friend friend) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>(friend);
        int count = friendService.count(queryWrapper);
        return success(count);
    }

    /**
     * 新增数据
     *
     * @param friend 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Friend friend) {
        return success(this.friendService.save(friend));
    }

    /**
     * 修改数据
     *
     * @param friend 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Friend friend) {
        return success(this.friendService.updateById(friend));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.friendService.removeByIds(idList));
    }










    /**
     * 1. 发送好友请求
     */
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        friendService.sendFriendRequest(dto.getToUserId(), dto.getVerifyMsg());
        return ResponseEntity.ok("好友请求已发送");
    }

    /**
     * 2. 获取收到的好友请求列表
     */
    @GetMapping("/requests")
    public ResponseEntity<?> getFriendRequests() {
        List<FriendRequestVO> list = friendService.getReceivedRequests();
        return ResponseEntity.ok(list);
    }

    /**
     * 3. 同意或拒绝好友请求
     */
    @PostMapping("/handle")
    public ResponseEntity<?> handleFriendRequest(@RequestBody HandleRequestDTO dto) {
        friendService.handleRequest(dto.getRequestId(), dto.getAction());
        return ResponseEntity.ok("操作成功");
    }

    /**
     * 4. 获取当前用户好友列表
     */
    @GetMapping("/list")
    public ResponseEntity<?> getFriendList() {
        List<FriendVO> list = friendService.getFriendList();
        return ResponseEntity.ok(list);
    }

    /**
     * 5. 删除好友
     */
    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long friendId) {
        friendService.deleteFriend(friendId);
        return ResponseEntity.ok("好友已删除");
    }

    /**
     * 获取当前用户所有好友的详细信息列表
     * 结合Friend和User表的信息，返回更丰富的好友数据
     */
    @GetMapping("/list/detail")
    public R selectAllFriendsWithDetails() {
        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        
        // 先获取当前用户的所有好友关系记录
        List<Friend> friends = this.friendService.list(new QueryWrapper<Friend>().eq("user_id", userId).eq("status", 1));
        
        // 如果没有好友，返回空列表
        if (friends.isEmpty()) {
            return success(new ArrayList<>());
        }
        
        // 提取所有好友ID
        List<Long> friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
        
        // 使用UserService批量查询好友的用户信息
        List<User> friendUsers = userService.listByIds(friendIds);
        
        // 将好友关系和用户信息组合成Map便于关联
        Map<Long, User> userMap = friendUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
        
        // 创建包含详细信息的结果列表
        List<Map<String, Object>> friendDetailsList = new ArrayList<>();
        
        for (Friend friend : friends) {
            User friendUser = userMap.get(friend.getFriendId());
            if (friendUser != null) {
                Map<String, Object> friendDetail = new HashMap<>();
                // 好友关系信息
                friendDetail.put("friendId", friend.getId());
                friendDetail.put("remark", friend.getRemark());
                friendDetail.put("addedTime", friend.getCreatedTime());
                friendDetail.put("status", friend.getStatus());
                
                // 用户详细信息
                friendDetail.put("userId", friendUser.getId());
                friendDetail.put("nickname", friendUser.getNickname());
                friendDetail.put("avatar", friendUser.getAvatar());
                friendDetail.put("gender", friendUser.getGender());
                friendDetail.put("phone", friendUser.getPhone());
                friendDetail.put("email", friendUser.getEmail());
                friendDetail.put("city", friendUser.getCity());
                friendDetail.put("myIntro", friendUser.getMyIntro());
                friendDetail.put("age", friendUser.getAge());
                friendDetail.put("school", friendUser.getSchool());
                friendDetail.put("userPic", friendUser.getUserPic());
                friendDetail.put("backImg", friendUser.getBackImg());
                friendDetail.put("isAnchor", friendUser.getIsAnchor());
                friendDetail.put("isPrivate", friendUser.getIsPrivate());
                
                friendDetailsList.add(friendDetail);
            }
        }
        
        return success(friendDetailsList);
    }
    
    /**
     * 获取指定好友的详细信息
     * @param friendId 好友关系ID
     * @return 好友的详细信息
     */
    @GetMapping("/{friendId}/detail")
    public R selectFriendDetail(@PathVariable Long friendId) {
        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        
        // 查询好友关系记录，确保是当前用户的好友
        Friend friend = this.friendService.getOne(new QueryWrapper<Friend>()
                .eq("id", friendId)
                .eq("user_id", userId)
                .eq("status", 1));
        
        if (friend == null) {
            return failed("未找到该好友信息");
        }
        
        // 使用UserService查询好友的用户信息
        User friendUser = userService.getById(friend.getFriendId());
        
        if (friendUser == null) {
            return failed("好友用户信息不存在");
        }
        
        // 创建包含详细信息的结果Map
        Map<String, Object> friendDetail = new HashMap<>();
        // 好友关系信息
        friendDetail.put("friendId", friend.getId());
        friendDetail.put("remark", friend.getRemark());
        friendDetail.put("addedTime", friend.getCreatedTime());
        friendDetail.put("status", friend.getStatus());
        
        // 用户详细信息
        friendDetail.put("userId", friendUser.getId());
        friendDetail.put("nickname", friendUser.getNickname());
        friendDetail.put("avatar", friendUser.getAvatar());
        friendDetail.put("gender", friendUser.getGender());
        friendDetail.put("phone", friendUser.getPhone());
        friendDetail.put("email", friendUser.getEmail());
        friendDetail.put("city", friendUser.getCity());
        friendDetail.put("myIntro", friendUser.getMyIntro());
        friendDetail.put("age", friendUser.getAge());
        friendDetail.put("school", friendUser.getSchool());
        friendDetail.put("userPic", friendUser.getUserPic());
        friendDetail.put("backImg", friendUser.getBackImg());
        friendDetail.put("isAnchor", friendUser.getIsAnchor());
        friendDetail.put("isPrivate", friendUser.getIsPrivate());
        
        return success(friendDetail);
    }




}

