package com.bruce.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.dto.CommentDto;
import com.bruce.service.CommentoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.IdentityHashMap;
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
@Autowired
private CommentoneService commentoneservice;



@GetMapping("/getfu")
    //获得所有父评论的接口
    public R getfu(){

    List<CommentDto>  x=commentoneservice.getcomment();


    return R.ok(x);



    }


@GetMapping("/getzi")
    //获得对应父评论的子评论
    public  R  getzi(@RequestParam("fu_id") int fu_id){
    List<CommentDto> m=commentoneservice.getcommenti(fu_id);

    return R.ok(m);
    }






    @GetMapping("/likeping")
    public R likeping(@RequestParam boolean like,int comment_id,int user_id) {

        if(like==true)
        {
//            insert comment_user into (comment_id,user_id,like_or_dislike) (&{comment_id},&{user_id},&{like})
        }else {
//            insert comment_user into (comment_id,user_id,like_or_dislike) (&{comment_id},&{user_id},&{dislike})

        }

        return R.ok(null);
    }


    @GetMapping("/insertpinglun")
    public R insertpinglun(@RequestParam int lastId) {

        if(lastId==-1)
        {
//            分配id  并且将其父字段设置为-1
            return R.ok(null);

        }else{
//            分配id  父id设置为lastid
            return R.ok(null);
        }

    }






    /**
     * 服务对象
//     */
//    @Resource
//    private CommentService commentService;
//
//    @GetMapping("/getcomment")
//    public R getData(@RequestParam int videoId) {
//      System.out.println(videoId);
//        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper();
//        queryWrapper.eq(Comment::getCommentVideoId,videoId);
//        return success(this.commentService.list(queryWrapper));
//    }

}

