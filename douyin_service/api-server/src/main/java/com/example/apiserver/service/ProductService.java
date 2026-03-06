package com.example.apiserver.service;

import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Product createProduct(Product product) {
        logger.info("开始保存商品到数据库，商品信息: {}", product);
        product.setStatus("active");

        // 初始化images列表，防止null
        if (product.getImages() == null) {
            product.setImages(new ArrayList<>());
        }

        // 处理userId，转换为User对象
        Long originalUserId = product.getUserId();
        
        // 如果userId为null，直接抛出异常，要求前端必须传递有效的userId
        if (originalUserId == null) {
            logger.error("发布商品失败：userId为null，前端必须传递有效的用户ID");
            throw new IllegalArgumentException("发布商品失败：必须传递有效的用户ID");
        }
        
        final Long finalUserId = originalUserId;
        
        try {
            // 只查找用户，不创建新用户
            Map<String, Object> userMap = userRepository.findById(finalUserId);
            if (userMap == null || userMap.isEmpty()) {
                throw new RuntimeException("用户不存在，userId: " + finalUserId);
            }
            User user = UserConverter.convertToUser(userMap);
            product.setUser(user);
        } catch (Exception e) {
            logger.error("处理用户信息失败，userId: {}, 错误: {}", finalUserId, e.getMessage());
            throw e; // 向上抛出异常，让前端知道具体错误
        }

        Product savedProduct = productRepository.save(product);
        logger.info("商品保存到数据库成功，ID: {}", savedProduct.getId());
        return savedProduct;
    }

    public List<Product> getAllProducts() {
        logger.info("开始从数据库查询所有商品");
        List<Product> products = productRepository.findAll();
        logger.info("从数据库查询所有商品成功，共找到 {} 个商品", products.size());
        return products;
    }

    public Product getProductById(Long id) {
        logger.info("开始从数据库查询商品，ID: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("商品不存在"));
        logger.info("从数据库查询商品成功，ID: {}, 商品信息: {}", id, product);
        return product;
    }

    public List<Product> getProductsByCategory(String category) {
        logger.info("开始从数据库查询分类商品，分类: {}", category);
        List<Product> products = productRepository.findByCategory(category);
        logger.info("从数据库查询分类商品成功，分类: {}, 共找到 {} 个商品", category, products.size());
        return products;
    }

    public List<Product> getProductsByUserId(Long userId) {
        logger.info("开始从数据库查询用户商品，用户ID: {}", userId);
        List<Product> products = productRepository.findByUser_Id(userId);
        logger.info("从数据库查询用户商品成功，用户ID: {}, 共找到 {} 个商品", userId, products.size());
        return products;
    }

    public List<Product> searchProducts(String keyword) {
        logger.info("开始从数据库搜索商品，关键词: {}", keyword);
        List<Product> products = productRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
        logger.info("从数据库搜索商品成功，关键词: {}, 共找到 {} 个商品", keyword, products.size());
        return products;
    }

    public Product updateProduct(Long id, Product product) {
        logger.info("开始更新数据库商品，ID: {}, 更新数据: {}", id, product);
        Product existingProduct = getProductById(id);
        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImages(product.getImages());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setLocation(product.getLocation());
        existingProduct.setStatus(product.getStatus());

        Product updatedProduct = productRepository.save(existingProduct);
        logger.info("数据库商品更新成功，ID: {}", updatedProduct.getId());
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        logger.info("开始从数据库删除商品，ID: {}", id);
        productRepository.deleteById(id);
        logger.info("从数据库删除商品成功，ID: {}", id);
    }
}
