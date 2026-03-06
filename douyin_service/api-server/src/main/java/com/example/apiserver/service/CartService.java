package com.example.apiserver.service;

import com.example.apiserver.entity.Cart;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.User;
import com.example.apiserver.repository.CartRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 获取用户购物车列表
     */
    public List<Cart> getCartListByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    /**
     * 添加商品到购物车
     */
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        // 检查用户是否存在
        Map<String, Object> userMap = userRepository.findById(userId);
        if (userMap == null || userMap.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = UserConverter.convertToUser(userMap);

        // 检查商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 检查商品是否已在购物车中
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartItem.isPresent()) {
            // 如果已存在，更新数量
            Cart cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + (quantity != null ? quantity : 1));
            return cartRepository.save(cart);
        } else {
            // 如果不存在，创建新的购物车项
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity != null ? quantity : 1);
            return cartRepository.save(cart);
        }
    }

    /**
     * 更新购物车商品数量
     */
    public Cart updateCartItemQuantity(Long cartItemId, Integer quantity) {
        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));

        if (quantity <= 0) {
            // 如果数量为0或负数，删除该购物车项
            cartRepository.delete(cart);
            return null;
        } else {
            // 更新数量
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        }
    }

    /**
     * 删除购物车项
     */
    public void deleteCartItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    /**
     * 清空购物车
     */
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    /**
     * 检查购物车中是否存在商品
     */
    public boolean checkProductInCart(Long userId, Long productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}