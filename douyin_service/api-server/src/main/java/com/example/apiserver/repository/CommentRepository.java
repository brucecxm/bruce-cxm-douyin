package com.example.apiserver.repository;

import com.example.apiserver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 根据商品ID查询评论列表，按创建时间倒序排列
     */
    List<Comment> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    /**
     * 统计商品的平均评分
     */
    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    /**
     * 统计商品不同评分的分布情况
     */
    @Query("SELECT c.rating AS rating, COUNT(c) AS count FROM Comment c WHERE c.product.id = :productId GROUP BY c.rating ORDER BY c.rating DESC")
    List<Map<String, Object>> getRatingDistributionByProductId(@Param("productId") Long productId);
    
    /**
     * 根据商品ID统计评论总数
     */
    Long countByProductId(Long productId);
}