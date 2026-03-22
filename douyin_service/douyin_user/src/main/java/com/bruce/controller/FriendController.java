package com.bruce.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.entity.User;
import com.bruce.pojo.FriendRequestDTO;
import com.bruce.pojo.FriendRequestVO;
import com.bruce.pojo.FriendVO;
import com.bruce.pojo.FriendDetailVO;
import com.bruce.pojo.HandleRequestDTO;
import com.bruce.pojo.Result;
import com.bruce.entity.Friend;
import com.bruce.service.FriendService;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result selectAll(
            @RequestParam(defaultValue = "1") int current, // 当前页，默认为1
            @RequestParam(defaultValue = "10") int size, // 每页大小，默认为10
            Friend friend) {

        // 创建一个分页对象
        Page<Friend> page = new Page<>(current, size);

        return Result.success(this.friendService.page(page, new QueryWrapper<>(friend)));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("/all")
    public Result selectAllFriends() {
        List<Friend> friends = this.friendService.list();
        return Result.success(friends);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Result selectOne(@PathVariable Serializable id) {
        return Result.success(this.friendService.getById(id));
    }

    /**
     * 搜索我的好友列表中的用户（用于我的好友列表搜索）
     */
    @GetMapping("/list/search/zan")
    public Result<?> searchMyFriends(@RequestParam String keyword) {
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        List<User> users = friendService.searchMyFriends(userId, keyword);
        return Result.success(users);
    }

    /**
     * 搜索我的好友列表中的用户（用于我的好友列表搜索）
     */
    @GetMapping("/list/search")
    public Result<?> searchUser(@RequestParam String keyword) {
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());
        List<User> users = friendService.searchMyFriends(userId, keyword);
        return Result.success(users);
    }





/**
 * 用户搜索接口 SQL 性能优化案例
 * isOptimize = false：优化前（慢查询、全表扫描、无索引、无分页）
 * isOptimize = true：优化后（索引查询、分页、逻辑拆分、性能提升10~100倍）
 */

    /**
     * 搜索用户（支持ID、用户名、手机号、邮箱）
     *
     * @param keyword  搜索关键词
     * @param pageNum  页码（默认第1页）
     * @param pageSize 每页条数（默认10条）
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result<?> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        // ===================== 优化开关：false=原始慢查询 | true=高性能查询 =====================
        boolean isOptimize = true;

        // 获取当前登录用户ID，业务要求：搜索结果必须排除自己
        Long currentUserId = Long.valueOf(StpUtil.getLoginIdAsString());

        // 初始化查询构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 基础条件：无论优化前后，都必须排除当前登录用户自己
        queryWrapper.ne("id", currentUserId);

        // ===================== 优化前：原始低效业务代码 =====================
        if (!isOptimize) {


//
//            EXPLAIN
//                    SELECT
//            id,
//                    email,
//                    user_pic,
//                    back_img,
//                    city,
//                    my_intro,
//                    age,
//                    school,
//                    username,
//                    gender,
//                    phone,
//                    password,
//                    nickname,
//                    avatar,
//                    create_time,
//                    update_time,
//                    permission_id,
//                    role_id,
//                    payment_password,
//                    is_anchor,
//                    is_private,
//                    last_login_time,
//                    balance,
//                    birthday,
//                    bio,
//                    created_at,
//                    updated_at
//            FROM
//                    user
//            WHERE
//                    (id <> 480971991428129
//            OR id = 1111
//            OR username LIKE '%1111%'
//            OR phone LIKE '%1111%'
//            OR email LIKE '%1111%')
//









            /*
             * 【优化前缺陷1】：ID 与模糊查询混合，无条件 OR 拼接
             * 生成 SQL：WHERE id != ? OR id = ? OR username LIKE %?% ...
             * 后果：OR 逻辑混乱，数据库无法使用索引，强制全表扫描
             */
            try {
                // 尝试将关键词转为数字（用户ID）
                Long id = Long.parseLong(keyword);
                // 无条件拼接 OR，破坏索引生效条件
                queryWrapper.or().eq("id", id);
            } catch (NumberFormatException ignored) {
                // 关键词不是数字，不处理
            }

            /*
             * 【优化前缺陷2】：全模糊查询 %keyword%
             * 最致命问题：% 开头的模糊查询 = 索引完全失效
             * 数据库必须逐行扫描全表，数据量越大越慢
             */
            queryWrapper.or().like("username", keyword)
                    .or().like("phone", keyword)
                    .or().like("email", keyword);

            /*
             * 【优化前缺陷3】：无分页，全表返回
             * 一次性加载所有匹配数据，内存溢出、网络IO极高
             */
            List<User> users = userService.list(queryWrapper);
            return Result.success(users);
        }

        // ===================== 优化后：高性能业务代码 =====================
        else {
            /*
             * 【优化点1】：查询优先级拆分 → ID优先精准查询
             * 原理：主键 ID 自带聚簇索引，等值查询是数据库最快查询方式
             * 只要关键词是数字，直接走主键查询，毫秒级返回
             */
            try {
                Long id = Long.parseLong(keyword);
                // 使用 and() 包裹条件，保证索引生效，不破坏查询结构
                queryWrapper.and(w -> w.eq("id", id));

                // ID精准匹配，直接返回结果，无需执行模糊查询
                List<User> users = userService.list(queryWrapper);
                if(users.size()==0)
                {
                    throw new NumberFormatException();
                }
                return Result.success(users);
            } catch (NumberFormatException e) {
                /*
                 * 【优化点2】：非数字关键词 → 走右模糊查询 keyword%
                 * 原理：like "%keyword%" → 索引失效
                 *      like "keyword%" → 索引生效（最左前缀原则）
                 * 性能提升：10倍~100倍
                 */
                queryWrapper.and(w -> w.likeRight("username", keyword)
                        .or().likeRight("phone", keyword)
                        .or().likeRight("email", keyword));
            }

            /*
             * 【优化点3】：分页查询（核心优化）
             * 原理：限制数据库扫描行数 + 减少网络传输数据量
             * 避免一次性加载全表数据，彻底解决慢查询
             */
            IPage<User> userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
            return Result.success(userPage);
        }
//
//
//        -- 用户搜索联合索引
//                -- 作用：覆盖 username、phone、email 三个搜索字段
//        -- 原理：让右模糊查询走索引，避免全表扫描
//        CREATE INDEX idx_user_search ON user (username, phone, email);
//



    }



    /**
     * 根据实体类属性查询所有匹配记录的数量
     *
     * @param friend 实体类对象，包含查询条件
     * @return 匹配的记录数量
     */
    @GetMapping("/count")
    public Result countByAttributes(Friend friend) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>(friend);
        int count = friendService.count(queryWrapper);
        return Result.success(count);
    }

    /**
     * 新增数据
     *
     * @param friend 实体对象
     * @return 新增结果
     */
    @PostMapping
    public Result insert(@RequestBody Friend friend) {
        return Result.success(this.friendService.save(friend));
    }

    /**
     * 修改数据
     *
     * @param friend 实体对象
     * @return 修改结果
     */
    @PutMapping
    public Result update(@RequestBody Friend friend) {
        return Result.success(this.friendService.updateById(friend));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public Result delete(@RequestParam("idList") List<Long> idList) {
        return Result.success(this.friendService.removeByIds(idList));
    }

    /**
     * 1. 发送好友请求
     */
    @PostMapping("/request")
    public Result<?> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        friendService.sendFriendRequest(dto.getToUserId(), dto.getVerifyMsg());
        return Result.success("好友请求已发送");
    }

    /**
     * 2. 获取收到的好友请求列表
     */
    @GetMapping("/requests")
    public Result<?> getFriendRequests() {
        List<FriendRequestVO> list = friendService.getReceivedRequests();
        return Result.success(list);
    }

    /**
     * 3. 同意或拒绝好友请求
     */
    @PostMapping("/handle")
    public Result<?> handleFriendRequest(@RequestBody HandleRequestDTO dto) {
        friendService.handleRequest(dto.getRequestId(), dto.getAction());
        return Result.success("操作成功");
    }

    /**
     * 4. 获取当前用户好友列表
     */
    @GetMapping("/list")
    public Result<?> getFriendList() {
        List<FriendVO> list = friendService.getFriendList();
        return Result.success(list);
    }

    /**
     * 5. 删除好友
     */
    @DeleteMapping("/{friendId}")
    public Result<?> deleteFriend(@PathVariable Long friendId) {
        friendService.deleteFriend(friendId);
        return Result.success("好友已删除");
    }

    /**
     * 获取当前用户所有好友的详细信息列表
     * 结合Friend和User表的信息，返回更丰富的好友数据
     */
    @GetMapping("/list/detail")
    public Result selectAllFriendsWithDetails() {
        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());

        // 先获取当前用户的所有好友关系记录
        List<Friend> friends = this.friendService
                .list(new QueryWrapper<Friend>().eq("user_id", userId).eq("status", 1));

        // 如果没有好友，返回空列表
        if (friends.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        // 提取所有好友ID
        List<Long> friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());

        // 使用UserService批量查询好友的用户信息
        List<User> friendUsers = userService.listByIds(friendIds);

        // 将好友关系和用户信息组合成Map便于关联
        Map<Long, User> userMap = friendUsers.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 创建包含详细信息的结果列表
        List<FriendDetailVO> friendDetailsList = new ArrayList<>();

        for (Friend friend : friends) {
            User friendUser = userMap.get(friend.getFriendId());
            if (friendUser != null) {
                FriendDetailVO friendDetail = new FriendDetailVO();
                // 好友关系信息
                friendDetail.setFriendId(friend.getId());
                friendDetail.setRemark(friend.getRemark());
                friendDetail.setAddedTime(friend.getCreatedTime());
                friendDetail.setStatus(friend.getStatus());

                // 用户详细信息
                friendDetail.setUserId(friendUser.getId());
                friendDetail.setNickname(friendUser.getNickname());
                friendDetail.setAvatar(friendUser.getAvatar());
                friendDetail.setGender(friendUser.getGender());
                friendDetail.setPhone(friendUser.getPhone());
                friendDetail.setEmail(friendUser.getEmail());
                friendDetail.setCity(friendUser.getCity());
                friendDetail.setMyIntro(friendUser.getMyIntro());
                friendDetail.setAge(friendUser.getAge());
                friendDetail.setSchool(friendUser.getSchool());
                friendDetail.setUserPic(friendUser.getUserPic());
                friendDetail.setBackImg(friendUser.getBackImg());
                friendDetail.setIsAnchor(friendUser.getIsAnchor());
                friendDetail.setIsPrivate(friendUser.getIsPrivate());

                friendDetailsList.add(friendDetail);
            }
        }

        return Result.success(friendDetailsList);
    }

    /**
     * 获取指定好友的详细信息
     *
     * @param friendId 好友关系ID
     * @return 好友的详细信息
     */
    @GetMapping("/{friendId}/detail")
    public Result selectFriendDetail(@PathVariable Long friendId) {
        // 获取当前登录用户ID
        Long userId = Long.valueOf(StpUtil.getLoginIdAsString());

        // 查询好友关系记录，确保是当前用户的好友
        Friend friend = this.friendService.getOne(new QueryWrapper<Friend>()
                .eq("id", friendId)
                .eq("user_id", userId)
                .eq("status", 1));

        if (friend == null) {
            return Result.error("未找到该好友信息");
        }

        // 使用UserService查询好友的用户信息
        User friendUser = userService.getById(friend.getFriendId());

        if (friendUser == null) {
            return Result.error("好友用户信息不存在");
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

        return Result.success(friendDetail);
    }

}
