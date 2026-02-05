package com.example.apiserver.service;

import com.bruce.utils.AuthUtils;
import com.example.apiserver.entity.Comment;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.CommentRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据商品ID获取评论列表
     */
    public List<Comment> getCommentsByProductId(Long productId) {
        return commentRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    /**
     * 获取商品的评论统计信息
     */
    public Map<String, Object> getCommentStatsByProductId(Long productId) {
        Map<String, Object> stats = new HashMap<>();

        // 获取平均评分
        Double averageRating = commentRepository.getAverageRatingByProductId(productId);
        stats.put("averageRating", averageRating != null ? Math.round(averageRating * 10) / 10.0 : 0.0);

        // 获取评论总数
        Long totalComments = commentRepository.countByProductId(productId);
        stats.put("totalComments", totalComments);

        // 获取评分分布
        List<Map<String, Object>> ratingDistribution = commentRepository.getRatingDistributionByProductId(productId);

        // 转换为前端需要的格式（包含所有评分等级1-5星）
        List<Map<String, Object>> formattedDistribution = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            final int rating = i;
            Optional<Map<String, Object>> distributionItem = ratingDistribution.stream()
                    .filter(item -> rating == ((Number) item.get("rating")).intValue())
                    .findFirst();

            Long count = distributionItem.isPresent() ? (Long) distributionItem.get().get("count") : 0L;
            int percentage = totalComments > 0 ? (int) Math.round((double) count / totalComments * 100) : 0;

            Map<String, Object> item = new HashMap<>();
            item.put("rating", rating);
            item.put("count", count);
            item.put("percentage", percentage);
            formattedDistribution.add(item);
        }

        stats.put("ratingDistribution", formattedDistribution);

        return stats;
    }

    /**
     * 创建新评论
     */
    @Transactional
    public Comment createComment(Long productId,  Integer rating, String content) {
        // 验证商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        // 获取当前登录用户ID
        Long loginUserId = AuthUtils.getCurrentUserId();
        // 验证用户是否存在
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 创建评论
        Comment comment = new Comment();
        comment.setProduct(product);
        comment.setUser(user);
        comment.setRating(rating);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    /**
     * 点赞评论
     */
    @Transactional
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));

        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }

    /**
     * 取消点赞评论
     */
    @Transactional
    public void unlikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));

        if (comment.getLikes() > 0) {
            comment.setLikes(comment.getLikes() - 1);
            commentRepository.save(comment);
        }
    }

    /**
     * 删除评论
     */
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("评论不存在");
        }
        commentRepository.deleteById(commentId);
    }
}
