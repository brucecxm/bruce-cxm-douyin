package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.data.LocalDataManager;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.ChatUser;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private LocalDataManager dataManager;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = LocalDataManager.getInstance();
        setupRecyclerViews();
        setupSearchFunction();
        loadAllProducts();
    }

    private void setupRecyclerViews() {
        // 设置分类RecyclerView
        categoryAdapter = new CategoryAdapter();
        binding.categoryRecyclerView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerView.setAdapter(categoryAdapter);

        // 设置商品RecyclerView
        productAdapter = new ProductAdapter();
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.productRecyclerView.setAdapter(productAdapter);

        // 设置分类点击监听
        categoryAdapter.setOnCategoryClickListener(category -> {
            if ("全部".equals(category)) {
                loadAllProducts();
            } else {
                loadProductsByCategory(category);
            }
        });

        // 设置商品点击监听
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // 商品项点击事件，可用于跳转到商品详情页
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).showMessage("商品详情功能开发中");
                }
            }

            @Override
            public void onAddToCartClick(Product product) {
                dataManager.addToCart(product, 1);
                // 显示添加成功提示
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).showMessage("已添加到购物车");
                }
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

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.ChatDetailFragment, bundle);
            }
        });

        // 加载分类数据
        List<String> categories = dataManager.getCategories();
        categories.add(0, "全部"); // 添加"全部"选项
        categoryAdapter.setCategories(categories);
    }

    private void setupSearchFunction() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadAllProducts();
                } else {
                    searchProducts(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadAllProducts() {
        List<Product> products = dataManager.getAllProducts();
        productAdapter.setProducts(products);
    }

    private void loadProductsByCategory(String category) {
        List<Product> products = dataManager.getProductsByCategory(category);
        productAdapter.setProducts(products);
    }

    private void searchProducts(String query) {
        List<Product> allProducts = dataManager.getAllProducts();
        List<Product> filteredProducts = new java.util.ArrayList<>();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        productAdapter.setProducts(filteredProducts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}