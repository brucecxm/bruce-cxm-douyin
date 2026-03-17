package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dict.Constant;
import com.bruce.entity.Comment;
import com.bruce.entity.User;
import com.bruce.entitys.CommentVO;
import com.bruce.pojo.Result;
import com.bruce.service.CommentService;
import com.bruce.service.IdService;
import com.bruce.service.UserFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2024-06-26 23:27:01
 */
@RestController
@RequestMapping("/video/comment")
public class CommentController extends ApiController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdService idService;
    @Autowired
    private UserFeignService userFeignService;
    // 使用线程池来异步上传图片
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // Controller 层
    @GetMapping("/getFatherComment")
    public Result<Page<CommentVO>> getFatherComment(@RequestParam Map<String, Object> params) {
        String videoId = String.valueOf(params.get("videoId"));
        int pageNum = Integer.parseInt(String.valueOf(params.getOrDefault(Constant.ApiParam.PAGE, 1)));
        int pageSize = Integer.parseInt(String.valueOf(params.getOrDefault(Constant.ApiParam.LIMIT, 10)));

        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getContentId, videoId)
                .isNull(Comment::getFatherId);

        // 1. 查询分页评论
        Page<Comment> commentPage = commentService.page(page, queryWrapper);

        // 2. 封装成VO，补充用户信息
        Page<CommentVO> voPage = new Page<>();
        // 复制分页基础信息
        voPage.setCurrent(commentPage.getCurrent());
        voPage.setSize(commentPage.getSize());
        voPage.setTotal(commentPage.getTotal());
        voPage.setPages(commentPage.getPages());

        // 3. 遍历评论，查询用户信息并赋值
        List<CommentVO> voList = commentPage.getRecords().stream().map(comment -> {
            CommentVO vo = new CommentVO();
            // 复制评论所有属性
            BeanUtils.copyProperties(comment, vo);

            // 4. 根据userId查询用户信息（你自己的userService）
            User user = (User) userFeignService.getUserById((comment.getUserId()));

            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setAvatar(user.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return Result.success(voPage);
    }
    @PostMapping("/addVideoComment")
    public Result<String> addVideoComment(@RequestBody Map<String, Object> params) {
        Comment comment = new Comment();

        // 1. 评论内容
        comment.setComment((String) params.get(Constant.ApiParam.COMMENT));

        // 2. 父评论ID
        Object fatherIdObj = params.get(Constant.ApiParam.FATHER_ID);
        Integer fatherId = null;
        if (fatherIdObj != null) {
            String strVal = fatherIdObj.toString().trim();
            if (!strVal.isEmpty()) {
                try {
                    fatherId = Integer.valueOf(strVal);
                } catch (NumberFormatException e) {
                    fatherId = null;
                }
            }
        }
        comment.setFatherId(fatherId);

        // 3. 评论ID
        long commentId = idService.SnowflakeGen();
        comment.setCommentId(commentId);

        // 4. 视频ID
        Object videoIdObj = params.get(Constant.ApiParam.VIDEO_ID);
        Long videoId = null;
        if (videoIdObj != null) {
            String str = videoIdObj.toString().trim();
            if (!str.isEmpty()) {
                try {
                    videoId = Long.valueOf(str);
                } catch (NumberFormatException e) {
                    videoId = null;
                }
            }
        }
        comment.setContentId(videoId);

        // 5. 用户ID
        Object userIdObj = params.get("userId");
        Long userId = null;
        if (userIdObj != null) {
            String str = userIdObj.toString().trim();
            if (!str.isEmpty()) {
                try {
                    userId = Long.valueOf(str);
                } catch (NumberFormatException e) {
                    userId = null;
                }
            }
        }
        comment.setUserId(userId);

        // ==============================================
        // 🔴 修复点 2：设置评论类型默认值
        // ==============================================
        comment.setCommentType(1L);

        // 保存
        commentService.save(comment);
        return Result.success("评论成功");
    }
    // @GetMapping("/getzi")
    // //获得对应父评论的子评论
    // public R getzi(@RequestParam("fu_id") int fu_id) {
    // List<CommentDto> m = commentservice.getcommenti(fu_id);
    // return R.ok(m);
    // }
    //

    @GetMapping("/likeping")
    public Result<Object> likeping(@RequestParam boolean like, int comment_id, int user_id) {
        if (like == true) {
            // insert comment_user into (comment_id,user_id,like_or_dislike)
            // (&{comment_id},&{user_id},&{like})
        } else {
            // insert comment_user into (comment_id,user_id,like_or_dislike)
            // (&{comment_id},&{user_id},&{dislike})
        }
        return Result.success(null);
    }

    @GetMapping("/insertpinglun")
    public Result<Object> insertpinglun(@RequestParam int lastId) {
        if (lastId == -1) {
            // 分配id 并且将其父字段设置为-1
            return Result.success(null);
        } else {
            // 分配id 父id设置为lastid
            return Result.success(null);
        }
    }
}
