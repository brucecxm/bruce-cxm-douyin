package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dao.chatInfoDao;
import com.bruce.entity.Video;
import com.bruce.pojo.FriendRequestDTO;
import com.bruce.pojo.FriendRequestVO;
import com.bruce.pojo.FriendVO;
import com.bruce.pojo.HandleRequestDTO;
import com.bruce.entity.Friend;
import com.bruce.pojo.UserVideoDTO;
import com.bruce.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
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
private chatInfoDao chatInfoDaoone;
    /**
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



    @Autowired
    private chatInfoDao chatInfoDaoo;

    // 后端代码
    @GetMapping("/getauth")
    public UserVideoDTO getauth(@RequestParam int userId) {
        // 从数据库获取指定用户ID的视频信息列表
        List<UserVideoDTO> userVideos = chatInfoDaoo.getUserVideosById(userId);

        // 创建一个新的 UserVideoDTO 实例，用于存储最终结果
        UserVideoDTO result = new UserVideoDTO();

        // 检查获取到的视频信息列表是否为空
        if (!userVideos.isEmpty()) {
            // 假设用户信息是相同的，将第一个 UserVideoDTO 的用户信息设置到结果中
            result.setUser(userVideos.get(0).getUser());

            // 使用流处理将用户视频列表转换为视频对象列表
            List<Video> videos = userVideos.stream()
                    .flatMap(userVideo -> userVideo.getVideos().stream()) // 获取每个 UserVideoDTO 的视频列表并展平
                    .map(video -> {
                        Video newVideo = new Video();
                        // 设置视频的 URL 和封面图
                        newVideo.setUrl(video.getUrl());
                        newVideo.setCover(video.getCover());
                        return newVideo; // 返回构造好的 Video 对象
                    })
                    .collect(Collectors.toList()); // 收集所有视频对象到列表中

            // 将视频列表设置到结果的 videos 属性中
            result.setVideos(videos);
        }

        // 返回最终的 UserVideoDTO 对象
        return result;
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
     * 根据实体类属性查询所有匹配记录
     *
     * @param friend 实体类对象，包含查询条件
     * @return 匹配的记录列表
     */
    @GetMapping("/search")
    public R selectByAttributes(Friend friend) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>(friend);
        List<Friend> friends = friendService.list(queryWrapper);
        return success(friends);
    }
    /**
     * 根据实体类属性查询所有匹配记录的数量
     *
     * @return 匹配的记录数量
     */
    @GetMapping("/queryUserInfo")
    public void queryUserInfo(String user_id) {

  List one= chatInfoDaoone.queryUserInfo(user_id);
        System.out.println(chatInfoDaoone.queryUserInfo(user_id).get(0).toString());

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






}

