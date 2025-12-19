package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.Product;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 获取传递的商品ID
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId == -1) {
            Toast.makeText(this, "商品不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 从后端API获取商品详情
        fetchProductDetailFromApi(productId);
    }

    private void initUI() {
        // 商品图片
        ImageView productImageView = findViewById(R.id.productDetailImageView);
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(productImageView);

        // 商品名称
        TextView productNameTextView = findViewById(R.id.productDetailNameTextView);
        productNameTextView.setText(product.getName());

        // 商品价格
        TextView productPriceTextView = findViewById(R.id.productDetailPriceTextView);
        productPriceTextView.setText("¥" + String.format("%.2f", product.getPrice()));

        // 评分
        TextView productRatingTextView = findViewById(R.id.productDetailRatingTextView);
        productRatingTextView.setText("⭐ " + product.getRating());

        // 位置
        TextView productLocationTextView = findViewById(R.id.productDetailLocationTextView);
        productLocationTextView.setText("位置: " + product.getLocation());

        // 成色
        TextView productConditionTextView = findViewById(R.id.productDetailConditionTextView);
        productConditionTextView.setText(product.getCondition());

        // 商品类型
        TextView productItemTypeTextView = findViewById(R.id.productItemTypeTextView);
        productItemTypeTextView.setText(product.getItemType());

        // 品牌
        TextView productBrandTextView = findViewById(R.id.productBrandTextView);
        productBrandTextView.setText("未知"); // 模拟数据，实际应该从product对象获取

        // 型号
        TextView productModelTextView = findViewById(R.id.productModelTextView);
        productModelTextView.setText("未知"); // 模拟数据，实际应该从product对象获取

        // 购买时间
        TextView productPurchaseDateTextView = findViewById(R.id.productPurchaseDateTextView);
        productPurchaseDateTextView.setText("未知"); // 模拟数据，实际应该从product对象获取

        // 联系方式
        TextView productContactPhoneTextView = findViewById(R.id.productContactPhoneTextView);
        productContactPhoneTextView.setText(product.getContactPhone());

        // 卖家信息
        TextView sellerNameTextView = findViewById(R.id.sellerNameTextView);
        sellerNameTextView.setText(product.getSellerName());

        // 卖家粉丝数
        TextView sellerFansCountTextView = findViewById(R.id.sellerFansCountTextView);
        sellerFansCountTextView.setText("粉丝: 120"); // 模拟数据

        // 卖家商品数
        TextView sellerProductCountTextView = findViewById(R.id.sellerProductCountTextView);
        sellerProductCountTextView.setText("商品: 8"); // 模拟数据

        // 商品评价数
        TextView productReviewCountTextView = findViewById(R.id.productReviewCountTextView);
        productReviewCountTextView.setText("(" + product.getReviewCount() + ")");

        // 商品描述
        TextView productDescriptionTextView = findViewById(R.id.productDetailDescriptionTextView);
        productDescriptionTextView.setText(product.getDescription());
    }

    private void setClickListeners() {
        // 返回按钮
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // 顶部聊天按钮
        Button topChatButton = findViewById(R.id.topChatButton);
        topChatButton.setOnClickListener(v -> handleChatClick());

        // 顶部加入购物车按钮
        Button topAddToCartButton = findViewById(R.id.topAddToCartButton);
        topAddToCartButton.setOnClickListener(v -> handleAddToCartClick());

        // 关注卖家按钮
        Button followSellerButton = findViewById(R.id.followSellerButton);
        followSellerButton.setOnClickListener(v -> handleFollowSellerClick());

        // 联系卖家按钮
        Button contactSellerButton = findViewById(R.id.contactSellerButton);
        contactSellerButton.setOnClickListener(v -> handleContactSellerClick());

        // 底部聊天按钮
        Button bottomChatButton = findViewById(R.id.bottomChatButton);
        bottomChatButton.setOnClickListener(v -> handleChatClick());

        // 底部加入购物车按钮
        Button bottomAddToCartButton = findViewById(R.id.bottomAddToCartButton);
        bottomAddToCartButton.setOnClickListener(v -> handleAddToCartClick());
    }

    // 从后端API获取商品详情
    private void fetchProductDetailFromApi(int productId) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", String.valueOf(productId));

        OkHttpManager.get(ApiConstants.SHOP_DETAIL_URL, params, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (response != null && response.isSuccess()) {
                    // 后端返回的data直接是商品对象，不需要再从Map中提取
                    // 将后端返回的商品数据转换为前端Product对象
                    product = convertToProduct(response.getData());
                    if (product != null) {
                        // 初始化UI
                        initUI();
                        // 设置点击事件
                        setClickListeners();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "商品转换失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "获取商品详情失败: " + (response != null ? response.getMessage() : "未知错误"), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void onFailure(String error) {
                Toast.makeText(ProductDetailActivity.this, "获取商品详情失败: " + error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // 将后端返回的商品数据转换为前端Product对象
    @SuppressWarnings("unchecked")
    private Product convertToProduct(Object data) {
        if (data instanceof Map) {
            Map<String, Object> productMap = (Map<String, Object>) data;
            Product product = new Product();
            
            // 转换后端Shop实体到前端Product
            Object shopIdObj = productMap.getOrDefault("shopId", 0);
            product.setId(shopIdObj instanceof Number ? ((Number) shopIdObj).intValue() : 0);
            
            Object shopNameObj = productMap.getOrDefault("shopName", "");
            product.setName(shopNameObj instanceof String ? (String) shopNameObj : "");
            
            Object shopDescObj = productMap.getOrDefault("shopDesc", "");
            product.setDescription(shopDescObj instanceof String ? (String) shopDescObj : "");
            
            // 商品详情（从shopInfo字段获取）
            Object shopInfoObj = productMap.getOrDefault("shopInfo", "");
            if (shopInfoObj instanceof String && !((String) shopInfoObj).isEmpty()) {
                // 如果有shopInfo字段，使用它作为详细描述
                product.setDescription((String) shopInfoObj);
            }
            
            // 后端shopPrice是Integer，前端是double，需要转换
            Object shopPriceObj = productMap.getOrDefault("shopPrice", 0);
            double price = 0.0;
            if (shopPriceObj instanceof Number) {
                price = ((Number) shopPriceObj).doubleValue();
            }
            product.setPrice(price);
            
            Object shopImgObj = productMap.getOrDefault("shopImg", "");
            product.setImageUrl(shopImgObj instanceof String ? (String) shopImgObj : "");
            
            Object shopTypeObj = productMap.getOrDefault("shopType", "");
            product.setCategory(shopTypeObj instanceof String ? (String) shopTypeObj : "");
            
            product.setStock(1); // 二手物品默认库存为1
            product.setRating(4.8); // 默认评分
            product.setReviewCount(120); // 默认评价数
            
            // 后端publisherId转为sellerId
            Object publisherIdObj = productMap.getOrDefault("publisherId", 0);
            int sellerId = 0;
            if (publisherIdObj instanceof Number) {
                sellerId = ((Number) publisherIdObj).intValue();
            }
            product.setSellerId(sellerId);
            
            product.setSellerName("商家" + publisherIdObj); // 临时使用publisherId作为商家名称
            product.setCondition("全新"); // 后端没有condition字段，默认全新
            product.setContactPhone("13800138000"); // 后端没有contactPhone字段，使用默认值
            product.setItemType("二手"); // 默认二手
            product.setLocation("北京市朝阳区"); // 默认位置
            
            return product;
        }
        return null;
    }

    private void handleChatClick() {
        // 跳转到聊天详情界面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigate_to", "chat_detail");
        intent.putExtra("seller_id", String.valueOf(product.getSellerId()));
        intent.putExtra("seller_name", product.getSellerName());
        intent.putExtra("product_id", String.valueOf(product.getId()));
        startActivity(intent);
    }

    private void handleAddToCartClick() {
        // 处理加入购物车逻辑
        Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
    }

    private void handleFollowSellerClick() {
        // 处理关注卖家逻辑
        Toast.makeText(this, "已关注卖家", Toast.LENGTH_SHORT).show();
    }

    private void handleContactSellerClick() {
        // 处理联系卖家逻辑
        Toast.makeText(this, "联系卖家功能开发中", Toast.LENGTH_SHORT).show();
    }
}