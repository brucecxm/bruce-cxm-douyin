package com.example.apiserver.repository;

import com.example.apiserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByUser_Id(Long userId);
    List<Product> findByTitleContainingOrDescriptionContaining(String title, String description);
}