package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.OkHttpManager;
import com.example.myapplication.utils.ApiConstants;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // 初始化控件
        initViews();
        
        // 设置点击事件
        setListeners();
    }
    
    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
    }
    
    private void setListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // 输入验证
        if (username.isEmpty()) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("userName", username);
        params.put("password", password);
        params.put("loginType", "password");
        params.put("isCheck", "false");
        
        // 发起登录请求
        OkHttpManager.postForm(ApiConstants.LOGIN_URL, params, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccess()) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            
                            // 保存用户信息和token到SharedPreferences
                            android.content.SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
                            
                            // 从响应中获取用户信息和token
                            if (response.getData() != null) {
                                try {
                                    // 使用Gson解析响应数据，获取用户ID和token
                                    String responseJson = new com.google.gson.Gson().toJson(response.getData());
                                    com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(responseJson).getAsJsonObject();
                                    
                                    // 提取token
                                    if (jsonObject.has("token")) {
                                        String token = jsonObject.get("token").getAsString();
                                        editor.putString("token", token);
                                    }
                                    
                                    // 提取用户ID
                                    if (jsonObject.has("userInfo")) {
                                        com.google.gson.JsonObject userInfoJson = jsonObject.getAsJsonObject("userInfo");
                                        if (userInfoJson.has("id")) {
                                            String userId = userInfoJson.get("id").getAsString();
                                            editor.putString("user_id", userId);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            
                            editor.apply();
                            
                            // 跳转到HTML显示页面
                                    startActivity(new Intent(LoginActivity.this, HtmlDisplayActivity.class));
                                    finish();
                        } else {
                            Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            
            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录失败: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
