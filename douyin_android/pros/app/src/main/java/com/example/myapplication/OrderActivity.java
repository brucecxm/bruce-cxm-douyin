package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.data.LocalDataManager;
import com.example.myapplication.databinding.ActivityOrderBinding;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.CartItem;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderRequest;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private LocalDataManager dataManager;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 设置标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("确认订单");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dataManager = LocalDataManager.getInstance();
        setupRecyclerView();
        setupSubmitButton();
        updateOrderDisplay();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter();
        binding.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderItemsRecyclerView.setAdapter(cartAdapter);

        // 禁用购物车项的编辑功能（只显示，不可编辑）
        cartAdapter.setOnCartItemClickListener(null);
    }

    private void setupSubmitButton() {
        binding.submitOrderButton.setOnClickListener(v -> {
            String address = binding.addressEditText.getText().toString().trim();
            String phone = binding.phoneEditText.getText().toString().trim();

            // 验证输入
            if (address.isEmpty()) {
                Toast.makeText(this, "请输入收货地址", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建订单
            List<CartItem> cartItems = dataManager.getCartItems();
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "购物车是空的", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建订单请求对象
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setAddress(address);
            orderRequest.setPhone(phone);
            orderRequest.setConsignee(dataManager.getCurrentUser().getNickname());
            orderRequest.setAmount(dataManager.getCartTotal());
            orderRequest.setPayMethod(1); // 1:微信支付，2:支付宝支付，这里默认微信支付
            
            // 添加订单明细
            List<OrderRequest.OrderDetailRequest> orderDetails = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                OrderRequest.OrderDetailRequest detail = new OrderRequest.OrderDetailRequest();
                detail.setProductId((long) cartItem.getProduct().getId());
                detail.setName(cartItem.getProduct().getName());
                detail.setQuantity(cartItem.getQuantity());
                detail.setAmount((int) cartItem.getProduct().getPrice());
                detail.setImage(cartItem.getProduct().getImageUrl());
                orderDetails.add(detail);
            }
            orderRequest.setOrderDetails(orderDetails);
            
            // 调用后端API创建订单
            OkHttpManager.postJson(ApiConstants.CREATE_ORDER_URL, orderRequest, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
                @Override
                public void onSuccess(ApiResponse response) {
                    runOnUiThread(() -> {
                        if (response.isSuccess()) {
                            // 订单创建成功，本地处理
                            // 复制购物车商品到订单（避免引用问题）
                            List<CartItem> orderItems = new ArrayList<>();
                            for (CartItem cartItem : cartItems) {
                                orderItems.add(new CartItem(cartItem.getProduct(), cartItem.getQuantity()));
                            }

                            Order order = new Order(0, orderItems, address, phone);
                            dataManager.addOrder(order);

                            // 清空购物车
                            dataManager.clearCart();

                            // 显示成功消息
                            Toast.makeText(OrderActivity.this, "订单提交成功！", Toast.LENGTH_LONG).show();

                            // 返回主页面
                            finish();
                        } else {
                            Toast.makeText(OrderActivity.this, "订单提交失败：" + response.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                
                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderActivity.this, "订单提交失败：" + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }

    private void updateOrderDisplay() {
        List<CartItem> cartItems = dataManager.getCartItems();
        cartAdapter.setCartItems(cartItems);

        double total = dataManager.getCartTotal();
        binding.totalPriceTextView.setText("¥" + String.format("%.2f", total));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
