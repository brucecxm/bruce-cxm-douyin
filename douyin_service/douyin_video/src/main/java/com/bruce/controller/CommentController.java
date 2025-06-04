package com.bruce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dict.Constant;
import com.bruce.dto.CommentDto;
import com.bruce.entity.Comment;
import com.bruce.service.CommentService;
import com.bruce.service.CommentoneService;
import com.bruce.service.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.*;

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
    // 使用线程池来异步上传图片
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // Controller 层
    @GetMapping("/getFatherComment")
    public R getFatherComment(@RequestParam Map<String, Object> params) {
        String videoId = String.valueOf(params.get("videoId"));
        int pageNum = Integer.parseInt(String.valueOf(params.getOrDefault(Constant.page, 1)));
        int pageSize = Integer.parseInt(String.valueOf(params.getOrDefault(Constant.limit, 10)));
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentId, videoId)
                .isNull(Comment::getFatherId);
        Page<Comment> result = commentService.page(page, queryWrapper);
        return R.ok(result);
    }


    @PostMapping("/addVideoComment")
    public R addVideoComment(@RequestBody Map<String, Object> params) {
        // 输出接收到的参数
        Comment comment = new Comment();
        comment.setComment((String) params.get(Constant.comment));
        comment.setFatherId((Integer) params.get(Constant.fatherId));
        long commentId = idService.SnowflakeGen();
        comment.setCommentId(commentId);
        comment.setContentId((Integer) params.get(Constant.videoId));
        comment.setUserId((Long) params.get("userId"));
        commentService.save(comment);
        // 这里可以处理评论逻辑
        return R.ok("评论成功");  // 返回成功响应
    }

//    @GetMapping("/getzi")
//    //获得对应父评论的子评论
//    public R getzi(@RequestParam("fu_id") int fu_id) {
//        List<CommentDto> m = commentservice.getcommenti(fu_id);
//        return R.ok(m);
//    }
//

    @GetMapping("/likeping")
    public R likeping(@RequestParam boolean like, int comment_id, int user_id) {
        if (like == true) {
//            insert comment_user into (comment_id,user_id,like_or_dislike) (&{comment_id},&{user_id},&{like})
        } else {
//            insert comment_user into (comment_id,user_id,like_or_dislike) (&{comment_id},&{user_id},&{dislike})
        }
        return R.ok(null);
    }

    @GetMapping("/insertpinglun")
    public R insertpinglun(@RequestParam int lastId) {
        if (lastId == -1) {
//            分配id  并且将其父字段设置为-1
            return R.ok(null);
        } else {
//            分配id  父id设置为lastid
            return R.ok(null);
        }
    }
}