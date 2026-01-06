package com.example.apiserver.service;

import com.example.apiserver.entity.RecentView;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.RecentViewRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecentViewService {

    @Autowired
    private RecentViewRepository recentViewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 默认最多保存的浏览记录数
    private static final int MAX_VIEW_COUNT = 20;

    /**
     * 添加或更新最近浏览记录
     */
    public RecentView addRecentView(Long userId, Long productId) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 检查是否已存在该商品的浏览记录
        return recentViewRepository.findByUserIdAndProductId(userId, productId)
                .map(existingView -> {
                    // 如果已存在，更新浏览时间
                    existingView.setViewedAt(new Date());
                    return recentViewRepository.save(existingView);
                })
                .orElseGet(() -> {
                    // 如果不存在，创建新的浏览记录
                    RecentView recentView = new RecentView();
                    recentView.setUser(user);
                    recentView.setProduct(product);
                    recentView.setViewedAt(new Date());
                    
                    // 保存新记录
                    RecentView savedView = recentViewRepository.save(recentView);
                    
                    // 检查是否超过最大记录数，如果超过则删除最旧的记录
                    List<RecentView> allViews = recentViewRepository.findByUserIdOrderByViewedAtDesc(userId);
                    if (allViews.size() > MAX_VIEW_COUNT) {
                        // 删除最旧的记录（最后一个）
                        recentViewRepository.delete(allViews.get(allViews.size() - 1));
                    }
                    
                    return savedView;
                });
    }

    /**
     * 获取用户最近浏览记录
     */
    public List<RecentView> getRecentViewsByUserId(Long userId) {
        return recentViewRepository.findByUserIdOrderByViewedAtDesc(userId);
    }

    /**
     * 获取用户最近浏览的前N条记录
     */
    public List<RecentView> getTopRecentViewsByUserId(Long userId, int limit) {
        return recentViewRepository.findTopNByUserIdOrderByViewedAtDesc(userId, PageRequest.of(0, limit));
    }

    /**
     * 删除用户的指定商品浏览记录
     */
    public void deleteRecentView(Long userId, Long productId) {
        recentViewRepository.deleteByUserIdAndProductId(userId, productId);
    }

    /**
     * 清空用户的最近浏览记录
     */
    public void clearRecentViews(Long userId) {
        recentViewRepository.deleteByUserId(userId);
    }

    // 请求参数内部类
    public static class RecentViewRequest {
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