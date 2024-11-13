package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dao.chatInfoDao;
import com.bruce.entity.Friend;
import com.bruce.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * (Friend)表控制层
 *
 * @author makejava
 * @since 2024-11-10 20:23:21
 */
@RestController
@RequestMapping("friend")
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
     * @param page 分页对象
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
     * @param friend 实体类对象，包含查询条件
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
}

