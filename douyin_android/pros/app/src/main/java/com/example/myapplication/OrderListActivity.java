package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.HashMap;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {

    private static final String TAG = "OrderListActivity";
    private RecyclerView rvOrders;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        // 设置标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("我的订单");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 初始化视图
        rvOrders = findViewById(R.id.rv_orders);
        emptyView = findViewById(R.id.empty_view);

        // 设置RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        // 这里可以添加订单列表适配器

        // 获取订单列表
        fetchOrders();
    }

    private void fetchOrders() {
        // 调用后端API获取订单列表
        Map<String, String> params = new HashMap<>();
        // 可以添加参数，如订单状态
        params.put("status", ""); // 空字符串表示获取所有状态的订单

        OkHttpManager.get(ApiConstants.GET_ORDERS_URL, params, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                runOnUiThread(() -> {
                    if (response.isSuccess()) {
                        // 处理订单列表数据
                        Log.d(TAG, "订单列表获取成功: " + response.getData());
                        // 这里可以更新RecyclerView适配器
                        // 暂时只显示一个Toast
                        Toast.makeText(OrderListActivity.this, "订单列表获取成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderListActivity.this, "订单列表获取失败: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderListActivity.this, "网络请求失败: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
