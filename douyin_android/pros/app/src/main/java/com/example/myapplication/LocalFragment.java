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
import com.example.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private Button allButton, electronicsButton, clothingButton, homeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_local, container, false);

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.local_product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化适配器
        productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        // 初始化筛选按钮
        allButton = view.findViewById(R.id.filter_all);
        electronicsButton = view.findViewById(R.id.filter_electronics);
        clothingButton = view.findViewById(R.id.filter_clothing);
        homeButton = view.findViewById(R.id.filter_home);

        // 设置筛选按钮点击事件
        setFilterButtonListeners();

        // 初始化模拟数据
        initMockData();

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
                Toast.makeText(getContext(), "已加入购物车", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChatClick(Product product) {
                // 跳转到聊天详情页面
                com.example.myapplication.model.ChatUser chatUser = new com.example.myapplication.model.ChatUser();
                chatUser.setUserId(String.valueOf(product.getSellerId()));
                chatUser.setUserName(product.getSellerName());
                chatUser.setAvatar("");
                chatUser.setLastMessage("");
                chatUser.setLastMessageTime(String.valueOf(System.currentTimeMillis()));
                chatUser.setUnreadCount(0);

                Bundle bundle = new Bundle();
                bundle.putSerializable("chatUser", chatUser);

                NavHostFragment.findNavController(LocalFragment.this)
                        .navigate(R.id.action_LocalFragment_to_ChatDetailFragment, bundle);
            }
        });

        return view;
    }

    // 初始化模拟数据
    private void initMockData() {
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

    // 设置筛选按钮点击事件
    private void setFilterButtonListeners() {
        // 全部
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterButtons();
                allButton.setBackgroundResource(R.drawable.filter_button_selected_bg);
                allButton.setTextColor(getResources().getColor(android.R.color.white));
                filteredProductList = new ArrayList<>(productList);
                productAdapter.setProducts(filteredProductList);
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
        filteredProductList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equals(category)) {
                filteredProductList.add(product);
            }
        }
        productAdapter.setProducts(filteredProductList);
    }
}