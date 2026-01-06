package com.example.apiserver.repository;

import com.example.apiserver.entity.RecentView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentViewRepository extends JpaRepository<RecentView, Long> {
    /**
     * 根据用户ID获取最近浏览记录，按浏览时间倒序排列
     */
    List<RecentView> findByUserIdOrderByViewedAtDesc(Long userId);

    /**
     * 根据用户ID获取最近浏览的前N条记录
     */
    List<RecentView> findTopNByUserIdOrderByViewedAtDesc(Long userId, org.springframework.data.domain.Pageable pageable);

    /**
     * 根据用户ID和商品ID查询最近浏览记录
     */
    Optional<RecentView> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * 删除用户的所有最近浏览记录
     */
    void deleteByUserId(Long userId);

    /**
     * 删除用户的指定商品浏览记录
     */
    void deleteByUserIdAndProductId(Long userId, Long productId);
}