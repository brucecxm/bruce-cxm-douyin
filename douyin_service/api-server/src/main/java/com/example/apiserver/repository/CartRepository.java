package com.example.apiserver.repository;

import com.example.apiserver.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * 根据用户ID获取购物车列表
     */
    List<Cart> findByUserId(Long userId);

    /**
     * 根据用户ID和商品ID查询购物车项
     */
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * 删除用户的所有购物车项
     */
    void deleteByUserId(Long userId);
}