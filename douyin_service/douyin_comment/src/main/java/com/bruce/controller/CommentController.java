package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.comment.entity.Comment;
import com.bruce.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2024-06-26 23:27:01
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends ApiController {













    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @GetMapping("/getcomment")
    public R getData(@RequestParam int videoId) {
      System.out.println(videoId);
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getCommentVideoId,videoId);
        return success(this.commentService.list(queryWrapper));
    }

}

