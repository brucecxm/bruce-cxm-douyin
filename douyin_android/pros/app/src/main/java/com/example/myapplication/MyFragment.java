package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.HashMap;
import java.util.Map;

public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TOKEN = "token";

    private TextView tvUsername;
    private TextView tvNickname;
    private TextView tvFansCount;
    private TextView tvFollowCount;
    private TextView tvBalance;
    private ImageView ivAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        // 初始化视图
        tvUsername = view.findViewById(R.id.tv_username);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvFollowCount = view.findViewById(R.id.tv_follow_count);
        tvBalance = view.findViewById(R.id.tv_balance);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        
        // 初始化快捷操作按钮
        // 发布商品（快捷按钮）
        view.findViewById(R.id.ll_my_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示我的发布
                Toast.makeText(getActivity(), "我的发布功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 我的订单
        view.findViewById(R.id.ll_my_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的订单页面
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(intent);
            }
        });
        
        // 我的地址
        view.findViewById(R.id.ll_my_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示我的地址
                Toast.makeText(getActivity(), "我的地址功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 我的收藏
        view.findViewById(R.id.ll_my_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示我的收藏
                Toast.makeText(getActivity(), "我的收藏功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 我的消息
        view.findViewById(R.id.ll_my_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示我的消息
                Toast.makeText(getActivity(), "我的消息功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 客服中心
        view.findViewById(R.id.ll_customer_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示客服中心
                Toast.makeText(getActivity(), "客服中心功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 设置
        view.findViewById(R.id.ll_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示设置
                Toast.makeText(getActivity(), "设置功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 发布商品按钮（快捷按钮）
        LinearLayout quickPublishLayout = view.findViewById(R.id.ll_my_publish);
        quickPublishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到发布商品页面
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });
        
        // 测试按钮点击事件
        view.findViewById(R.id.Test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到测试页面
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });
        
        // HTML显示按钮点击事件
        view.findViewById(R.id.ll_html_display).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到HTML显示页面
                Intent intent = new Intent(getActivity(), HtmlDisplayActivity.class);
                startActivity(intent);
            }
        });
        
        // 初始化退出登录按钮
        view.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // 获取用户信息
        fetchUserInfo();

        return view;
    }

    // 获取用户信息
    private void fetchUserInfo() {
        // 从SharedPreferences中获取登录返回的用户ID
        android.content.SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs",
                android.content.Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        String token = sharedPreferences.getString("token", "");

        Map<String, String> params = new HashMap<>();

        // 如果没有获取到用户ID，使用固定值1作为默认值
        if (userId.isEmpty()) {
            userId = "1";
            Log.d(TAG, "未获取到用户ID，使用默认值1");
        } else {
            Log.d(TAG, "获取到用户ID: " + userId);
        }

        params.put("userId", userId);
        params.put("type", "bloom");
        Log.d(TAG, "调用userinfo接口，参数: " + params);

        OkHttpManager.get(ApiConstants.USER_INFO_URL, params, ApiResponse.class,
                new OkHttpManager.NetworkCallback<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        Log.d(TAG, "userinfo接口调用成功，响应: " + response);
                        if (response != null && response.isSuccess()) {
                            // 更新UI
                            updateUserInfoUI(response);
                        } else {
                            Log.e(TAG, "userinfo接口调用失败，错误信息: " + (response != null ? response.getMessage() : "未知错误"));
                            showError(response != null ? response.getMessage() : "获取用户信息失败");
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e(TAG, "userinfo接口网络请求失败，错误信息: " + error);
                        showError(error);
                    }
                });
    }

    // 更新用户信息UI
    private void updateUserInfoUI(ApiResponse response) {
        // 直接在UI线程中更新，移除isAdded()检查
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "开始更新用户信息UI");
                        // 从响应中获取用户信息
                        if (response != null) {
                            Log.d(TAG, "响应不为null，code: " + response.getCode() + ", message: " + response.getMessage());

                            if (response.getData() != null) {
                                Log.d(TAG, "响应数据不为null，类型: " + response.getData().getClass().getName());

                                // 直接将data作为Map类型处理
                                if (response.getData() instanceof java.util.Map) {
                                    java.util.Map<String, Object> userInfoMap = (java.util.Map<String, Object>) response
                                            .getData();
                                    Log.d(TAG, "响应数据是Map类型，包含的键: " + userInfoMap.keySet());

                                    // 解析用户信息
                                    String username = (String) userInfoMap.getOrDefault("username", "");
                                    String nickname = (String) userInfoMap.getOrDefault("nickname", "");
                                    String avatar = (String) userInfoMap.getOrDefault("avatar", "");
                                    Object balanceObj = userInfoMap.getOrDefault("balance", 0.0);
                                    double balance = balanceObj instanceof Number ? ((Number) balanceObj).doubleValue()
                                            : 0.0;

                                    // 获取粉丝数和关注数
                                    Object fansCountObj = userInfoMap.getOrDefault("fansCount", 0);
                                    int fansCount = fansCountObj instanceof Number ? ((Number) fansCountObj).intValue()
                                            : 0;
                                    Object followingObj = userInfoMap.getOrDefault("following", 0);
                                    int following = followingObj instanceof Number ? ((Number) followingObj).intValue()
                                            : 0;

                                    Log.d(TAG,
                                            "解析出的用户信息: username=" + username + ", nickname=" + nickname + ", balance="
                                                    + balance + ", fansCount=" + fansCount + ", following="
                                                    + following);

                                    // 更新UI
                                    tvUsername.setText(username);
                                    tvNickname.setText(nickname);
                                    tvBalance.setText(String.format("余额: %.2f", balance));
                                    tvFansCount.setText("粉丝: " + fansCount);
                                    tvFollowCount.setText("关注: " + following);

                                    // 加载头像
                                    if (avatar != null && !avatar.isEmpty()) {
                                        // 移除头像URL中的空格和反引号
                                        avatar = avatar.trim().replace("`", "");
                                        Glide.with(getActivity())
                                                .load(avatar)
                                                .placeholder(R.drawable.ic_launcher_foreground)
                                                .error(R.drawable.ic_launcher_foreground)
                                                .into(ivAvatar);
                                    }
                                } else {
                                    Log.d(TAG, "响应数据不是Map类型，而是: " + response.getData().getClass().getName());
                                }
                            } else {
                                Log.d(TAG, "响应数据为null");
                            }
                        } else {
                            Log.d(TAG, "响应为null");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "解析用户信息失败: " + e.getMessage());
                        e.printStackTrace();
                        showError("解析用户信息失败");
                    }
                }
            });
        }
    }

    // 显示错误信息
    private void showError(String error) {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    // 退出登录
    private void logout() {
        // 清除SharedPreferences中的用户信息和token
        android.content.SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs",
                android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // 显示退出登录成功提示
        Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT).show();
        
        // 跳转到登录页面
        android.content.Intent intent = new android.content.Intent(getActivity(), LoginActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        
        // 结束当前MainActivity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}