package com.example.apiserver.service;

import com.example.apiserver.entity.Favorite;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.FavoriteRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Favorite addFavorite(Long userId, Long productId) {
        // 检查是否已收藏
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("已经收藏过该商品");
        }

        // 检查用户是否存在
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        // 检查商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 创建收藏对象
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeFavorite(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("未收藏该商品"));
        favoriteRepository.delete(favorite);
    }

    public Boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }

    // 订单请求参数类
    public static class FavoriteRequest {
        private Long userId;
        private Long productId;

        // getter 和 setter 方法
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }
}