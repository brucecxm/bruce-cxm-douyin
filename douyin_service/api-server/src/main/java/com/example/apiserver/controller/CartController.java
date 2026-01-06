package com.example.apiserver.controller;

import com.example.apiserver.common.Response;
import com.example.apiserver.entity.Cart;
import com.example.apiserver.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取用户购物车列表
     */
    @GetMapping("/{userId}")
    public Response<List<Cart>> getCartList(@PathVariable Long userId) {
        try {
            List<Cart> cartList = cartService.getCartListByUserId(userId);
            return Response.success(cartList);
        } catch (Exception e) {
            return Response.error("获取购物车失败: " + e.getMessage());
        }
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping
    public Response<Cart> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            Cart cart = cartService.addToCart(
                    cartRequest.getUserId(),
                    cartRequest.getProductId(),
                    cartRequest.getQuantity()
            );
            return Response.success("添加到购物车成功", cart);
        } catch (Exception e) {
            return Response.error("添加到购物车失败: " + e.getMessage());
        }
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{cartItemId}")
    public Response<Cart> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody QuantityRequest quantityRequest) {
        try {
            Cart cart = cartService.updateCartItemQuantity(
                    cartItemId,
                    quantityRequest.getQuantity()
            );
            return Response.success("更新购物车数量成功", cart);
        } catch (Exception e) {
            return Response.error("更新购物车数量失败: " + e.getMessage());
        }
    }

    /**
     * 删除购物车项
     */
    @DeleteMapping("/{cartItemId}")
    public Response<Void> deleteCartItem(@PathVariable Long cartItemId) {
        try {
            cartService.deleteCartItem(cartItemId);
            return Response.<Void>success("删除购物车项成功", null);
        } catch (Exception e) {
            return Response.error("删除购物车项失败: " + e.getMessage());
        }
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear/{userId}")
    public Response<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return Response.<Void>success("清空购物车成功", null);
        } catch (Exception e) {
            return Response.error("清空购物车失败: " + e.getMessage());
        }
    }

    /**
     * 检查商品是否在购物车中
     */
    @GetMapping("/check/{userId}/{productId}")
    public Response<Boolean> checkProductInCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        try {
            boolean isInCart = cartService.checkProductInCart(userId, productId);
            return Response.success(isInCart);
        } catch (Exception e) {
            return Response.error("检查商品失败: " + e.getMessage());
        }
    }

    // 请求参数内部类
    public static class CartRequest {
        private Long userId;
        private Long productId;
        private Integer quantity;

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

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class QuantityRequest {
        private Integer quantity;

        // getter 和 setter 方法
        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}