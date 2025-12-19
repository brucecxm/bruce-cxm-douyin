package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.data.LocalDataManager;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.ChatUser;
import com.example.myapplication.model.Product;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private Button allButton, electronicsButton, clothingButton, homeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局文件
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.communityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化适配器
        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        // 初始化筛选按钮
        allButton = view.findViewById(R.id.community_filter_all);
        electronicsButton = view.findViewById(R.id.community_filter_electronics);
        clothingButton = view.findViewById(R.id.community_filter_clothing);
        homeButton = view.findViewById(R.id.community_filter_home);

        // 设置筛选按钮点击事件
        setFilterButtonListeners();

        // 从后端API获取数据
        fetchProductsFromApi();

        // 设置商品点击事件
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // 跳转到商品详情页
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                // 处理加入购物车逻辑
                LocalDataManager.getInstance().addToCart(product, 1);
                Toast.makeText(getContext(), "已加入购物车", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChatClick(Product product) {
                // 跳转到聊天详情页面
                ChatUser chatUser = new ChatUser();
                chatUser.setUserId(String.valueOf(product.getSellerId()));
                chatUser.setUserName(product.getSellerName());
                chatUser.setAvatar("");
                chatUser.setLastMessage("");
                chatUser.setLastMessageTime(String.valueOf(System.currentTimeMillis()));
                chatUser.setUnreadCount(0);

                Bundle bundle = new Bundle();
                bundle.putSerializable("chatUser", chatUser);

                NavHostFragment.findNavController(CommunityFragment.this)
                        .navigate(R.id.action_CommunityFragment_to_ChatDetailFragment, bundle);
            }
        });
    }

    // 从后端API获取商品列表
    private void fetchProductsFromApi() {
        OkHttpManager.get(ApiConstants.SHOP_LIST_URL, null, ApiResponse.class,
                new OkHttpManager.NetworkCallback<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        if (response != null && response.isSuccess()) {
                            // 后端返回的data直接是商品列表，不需要再从Map中提取
                            // 将后端返回的商品数据转换为前端Product对象
                            productList = convertToProductList(response.getData());
                            // 初始显示全部商品
                            filteredProductList = new ArrayList<>(productList);
                            productAdapter.setProducts(filteredProductList);
                        } else {
                            Toast.makeText(getContext(),
                                    "获取商品列表失败: " + (response != null ? response.getMessage() : "未知错误"),
                                    Toast.LENGTH_SHORT).show();
                            // 加载模拟数据
                            loadMockData();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getContext(), "获取商品列表失败: " + error, Toast.LENGTH_SHORT).show();
                        // 加载模拟数据
                        loadMockData();
                    }
                });
    }

    // 加载模拟数据
    private void loadMockData() {
        productList = new ArrayList<>();
        // 添加模拟商品数据
        Product product1 = new Product(1, "iPhone 14 Pro", "95成新，无划痕，配件齐全", 5999.0, "https://via.placeholder.com/300x300?text=iPhone+14+Pro", "数码", 1, 4.8, 120);
        product1.setSellerId(1);
        product1.setSellerName("张三");
        product1.setLocation("广州");
        product1.setCondition("95新");
        product1.setContactPhone("13800138001");
        product1.setItemType("二手");
        productList.add(product1);

        Product product2 = new Product(2, "小米13 Ultra", "9成新，轻微使用痕迹", 3999.0, "https://via.placeholder.com/300x300?text=Xiaomi+13+Ultra", "数码", 1, 4.6, 80);
        product2.setSellerId(2);
        product2.setSellerName("李四");
        product2.setLocation("深圳");
        product2.setCondition("9成新");
        product2.setContactPhone("13800138002");
        product2.setItemType("二手");
        productList.add(product2);

        Product product3 = new Product(3, "Nike Air Jordan", "全新未穿，原盒包装", 899.0, "https://via.placeholder.com/300x300?text=Nike+Air+Jordan", "服饰", 1, 5.0, 30);
        product3.setSellerId(3);
        product3.setSellerName("王五");
        product3.setLocation("北京");
        product3.setCondition("全新");
        product3.setContactPhone("13800138003");
        product3.setItemType("二手");
        productList.add(product3);

        Product product4 = new Product(4, "宜家书桌", "8成新，稳固耐用", 499.0, "https://via.placeholder.com/300x300?text=IKEA+Desk", "家居", 1, 4.5, 50);
        product4.setSellerId(4);
        product4.setSellerName("赵六");
        product4.setLocation("上海");
        product4.setCondition("8成新");
        product4.setContactPhone("13800138004");
        product4.setItemType("二手");
        productList.add(product4);

        Product product5 = new Product(5, "MacBook Pro 16", "95成新，M1芯片", 12999.0, "https://via.placeholder.com/300x300?text=MacBook+Pro+16", "数码", 1, 4.9, 60);
        product5.setSellerId(5);
        product5.setSellerName("孙七");
        product5.setLocation("杭州");
        product5.setCondition("95新");
        product5.setContactPhone("13800138005");
        product5.setItemType("二手");
        productList.add(product5);

        Product product6 = new Product(6, "Adidas运动鞋", "9成新，尺码42", 499.0, "https://via.placeholder.com/300x300?text=Adidas+Shoes", "服饰", 1, 4.7, 40);
        product6.setSellerId(6);
        product6.setSellerName("周八");
        product6.setLocation("成都");
        product6.setCondition("9成新");
        product6.setContactPhone("13800138006");
        product6.setItemType("二手");
        productList.add(product6);
        // 初始显示全部商品
        filteredProductList = new ArrayList<>(productList);
        productAdapter.setProducts(filteredProductList);
    }

    // 将后端返回的商品数据转换为前端Product对象
    @SuppressWarnings("unchecked")
    private List<Product> convertToProductList(Object data) {
        List<Product> products = new ArrayList<>();
        if (data instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            for (Map<String, Object> itemMap : dataList) {
                Product product = new Product();

                // 转换后端Shop实体到前端Product
                Object shopIdObj = itemMap.getOrDefault("shopId", 0);
                product.setId(shopIdObj instanceof Number ? ((Number) shopIdObj).intValue() : 0);

                Object shopNameObj = itemMap.getOrDefault("shopName", "");
                product.setName(shopNameObj instanceof String ? (String) shopNameObj : "");

                Object shopDescObj = itemMap.getOrDefault("shopDesc", "");
                product.setDescription(shopDescObj instanceof String ? (String) shopDescObj : "");

                // 商品详情（从shopInfo字段获取）
                Object shopInfoObj = itemMap.getOrDefault("shopInfo", "");
                if (shopInfoObj instanceof String && !((String) shopInfoObj).isEmpty()) {
                    // 如果有shopInfo字段，使用它作为详细描述
                    product.setDescription((String) shopInfoObj);
                }

                // 后端shopPrice是Integer，前端是double，需要转换
                Object shopPriceObj = itemMap.getOrDefault("shopPrice", 0);
                double price = 0.0;
                if (shopPriceObj instanceof Number) {
                    price = ((Number) shopPriceObj).doubleValue();
                }
                product.setPrice(price);

                Object shopImgObj = itemMap.getOrDefault("shopImg", "");
                product.setImageUrl(shopImgObj instanceof String ? (String) shopImgObj : "");

                Object shopTypeObj = itemMap.getOrDefault("shopType", "");
                product.setCategory(shopTypeObj instanceof String ? (String) shopTypeObj : "");

                product.setStock(1); // 二手物品默认库存为1
                product.setRating(4.8); // 默认评分
                product.setReviewCount(120); // 默认评价数

                // 后端publisherId转为sellerId
                Object publisherIdObj = itemMap.getOrDefault("publisherId", 0);
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

                products.add(product);
            }
        }
        return products;
    }

    // 设置筛选按钮点击事件
    private void setFilterButtonListeners() {
        // 全部
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterButtons();
                allButton.setBackgroundResource(R.drawable.filter_button_selected_bg);
                allButton.setTextColor(getResources().getColor(android.R.color.white));
                if (productList != null) {
                    filteredProductList = new ArrayList<>(productList);
                    productAdapter.setProducts(filteredProductList);
                }
            }
        });

        // 数码
        electronicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterButtons();
                electronicsButton.setBackgroundResource(R.drawable.filter_button_selected_bg);
                electronicsButton.setTextColor(getResources().getColor(android.R.color.white));
                filterProductsByCategory("数码");
            }
        });

        // 服饰
        clothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterButtons();
                clothingButton.setBackgroundResource(R.drawable.filter_button_selected_bg);
                clothingButton.setTextColor(getResources().getColor(android.R.color.white));
                filterProductsByCategory("服饰");
            }
        });

        // 家居
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterButtons();
                homeButton.setBackgroundResource(R.drawable.filter_button_selected_bg);
                homeButton.setTextColor(getResources().getColor(android.R.color.white));
                filterProductsByCategory("家居");
            }
        });
    }

    // 重置筛选按钮样式
    private void resetFilterButtons() {
        // 重置所有按钮样式
        allButton.setBackgroundResource(R.drawable.filter_button_bg);
        allButton.setTextColor(getResources().getColor(android.R.color.black));
        electronicsButton.setBackgroundResource(R.drawable.filter_button_bg);
        electronicsButton.setTextColor(getResources().getColor(android.R.color.black));
        clothingButton.setBackgroundResource(R.drawable.filter_button_bg);
        clothingButton.setTextColor(getResources().getColor(android.R.color.black));
        homeButton.setBackgroundResource(R.drawable.filter_button_bg);
        homeButton.setTextColor(getResources().getColor(android.R.color.black));
    }

    // 按分类筛选商品
    private void filterProductsByCategory(String category) {
        if (productList != null) {
            filteredProductList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getCategory().equals(category)) {
                    filteredProductList.add(product);
                }
            }
            productAdapter.setProducts(filteredProductList);
        }
    }
}