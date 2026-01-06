package com.example.apiserver.repository;

import com.example.apiserver.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"address", "product"})
    List<Order> findByUserId(Long userId);
    
    @EntityGraph(attributePaths = {"address", "product"})
    Order findByProductId(Long productId);
    
    @EntityGraph(attributePaths = {"address", "product"})
    Order findByOrderNo(String orderNo);
}