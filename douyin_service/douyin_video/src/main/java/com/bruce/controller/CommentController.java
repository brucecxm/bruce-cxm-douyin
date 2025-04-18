package com.bruce.controller;



import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.bruce.dto.CommentDto;
import com.bruce.entity.Comment;
import com.bruce.service.CommentoneService;
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
@RequestMapping("/comment")
public class CommentController extends ApiController {
@Autowired
private CommentoneService commentoneservice;

    @Autowired
    private RedisTemplate redisTemplate;


    // 使用线程池来异步上传图片
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addComment(@RequestParam Map<String, String> params,
                                                          @RequestParam(required = false) List<MultipartFile> images) {
        // 获取参数
        String commentType = params.get("comment_type");
        String content = params.get("content");
        // 获取 comment_id 参数值
        String commentIdStr = params.get("comment_id");
        Long commentId= Long.valueOf(000000000);
// 检查 comment_id 是否为 null 或非数字
        if (commentIdStr != null && commentIdStr.matches("\\d+")) {
             commentId = Long.valueOf(commentIdStr); // 转换为 Long 类型
            // 继续你的逻辑
        } else {
            // 处理参数无效的情况，比如抛出异常或者赋一个默认值
            System.out.println("Invalid comment_id parameter");
        }
        Boolean isFollow = Boolean.valueOf(params.get("is_follow"));

        // 处理文件上传
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            List<Future<String>> futures = new ArrayList<>();
            // 异步上传图片
            for (MultipartFile image : images) {
//                futures.add(executorService.submit(() -> uploadImage(image)));
            }

            // 等待上传结果
            for (Future<String> future : futures) {
                try {
                    imageUrls.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        // 构建评论对象
        Comment comment = new Comment();
        comment.setCommentType(commentType);
        comment.setIsFollow(isFollow);
        comment.setCommentId(commentId);
        comment.setContent(content);
        comment.setImageUrls(imageUrls);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        // todo 将评论保存到数据库，简化为存储到 Redis 中模拟（实际应用中这里是存储到数据库）
        String redisKey = "post_comments:" + commentId;
        redisTemplate.opsForList().leftPush(redisKey, comment);

        // 更新缓存：更新评论列表缓存
        redisTemplate.expire(redisKey, 3600, TimeUnit.SECONDS);  // 设置缓存过期时间1小时

        // 构建返回的 Map
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("comment", comment);

        return ResponseEntity.ok(response);
    }

@GetMapping("/getfu")
    //获得所有父评论的接口
    public R getfu(Map params){

    String videoid = String.valueOf(params.get("videoid"));
int videoId = Integer.valueOf(videoid);
    List<Map>  x=commentoneservice.getcomment(videoId);


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

