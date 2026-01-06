package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Comment;
import com.example.apiserver.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 根据商品ID获取评论列表
     */
    @GetMapping("/product/{productId}")
    public Response<List<Comment>> getCommentsByProductId(@PathVariable Long productId) {
        logger.info("开始根据商品ID查询评论列表，商品ID: {}", productId);
        try {
            List<Comment> comments = commentService.getCommentsByProductId(productId);
            logger.info("根据商品ID查询评论列表成功，商品ID: {}, 共找到 {} 条评论", productId, comments.size());
            return Response.success(comments);
        } catch (Exception e) {
            logger.error("根据商品ID查询评论列表失败，商品ID: {}, 错误: {}", productId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 获取商品评论统计信息
     */
    @GetMapping("/stats/{productId}")
    public Response<Map<String, Object>> getCommentStatsByProductId(@PathVariable Long productId) {
        logger.info("开始获取商品评论统计信息，商品ID: {}", productId);
        try {
            Map<String, Object> stats = commentService.getCommentStatsByProductId(productId);
            logger.info("获取商品评论统计信息成功，商品ID: {}", productId);
            return Response.success(stats);
        } catch (Exception e) {
            logger.error("获取商品评论统计信息失败，商品ID: {}, 错误: {}", productId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 提交评论
     */
    @PostMapping
    public Response<Comment> createComment(@RequestBody Map<String, Object> requestBody) {
        try {
            // 从请求体中获取参数
            Long productId = Long.parseLong(requestBody.get("productId").toString());
            Long userId = Long.parseLong(requestBody.get("userId").toString());
            Integer rating = Integer.parseInt(requestBody.get("rating").toString());
            String content = requestBody.get("content").toString();
            
            logger.info("开始创建评论，商品ID: {}, 用户ID: {}, 评分: {}, 内容: {}", productId, userId, rating, content);
            
            // 调用服务创建评论
            Comment comment = commentService.createComment(productId, userId, rating, content);
            
            logger.info("创建评论成功，评论ID: {}", comment.getId());
            return Response.success(comment);
        } catch (Exception e) {
            logger.error("创建评论失败，错误: {}", e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/{commentId}/like")
    public Response<Void> likeComment(@PathVariable Long commentId) {
        logger.info("开始点赞评论，评论ID: {}", commentId);
        try {
            commentService.likeComment(commentId);
            logger.info("点赞评论成功，评论ID: {}", commentId);
            return Response.success();
        } catch (Exception e) {
            logger.error("点赞评论失败，评论ID: {}, 错误: {}", commentId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 取消点赞评论
     */
    @DeleteMapping("/{commentId}/like")
    public Response<Void> unlikeComment(@PathVariable Long commentId) {
        logger.info("开始取消点赞评论，评论ID: {}", commentId);
        try {
            commentService.unlikeComment(commentId);
            logger.info("取消点赞评论成功，评论ID: {}", commentId);
            return Response.success();
        } catch (Exception e) {
            logger.error("取消点赞评论失败，评论ID: {}, 错误: {}", commentId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public Response<Void> deleteComment(@PathVariable Long commentId) {
        logger.info("开始删除评论，评论ID: {}", commentId);
        try {
            commentService.deleteComment(commentId);
            logger.info("删除评论成功，评论ID: {}", commentId);
            return Response.success();
        } catch (Exception e) {
            logger.error("删除评论失败，评论ID: {}, 错误: {}", commentId, e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
}