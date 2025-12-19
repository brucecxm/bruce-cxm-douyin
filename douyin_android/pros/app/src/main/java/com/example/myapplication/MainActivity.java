package com.example.myapplication;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.data.LocalDataManager;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private LocalDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        
        // 设置底部导航栏的目的地
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.CommunityFragment,
                R.id.ChatFragment,
                R.id.LocalFragment,
                R.id.MyFragment
        ).build();
        
        // 关联ActionBar与NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        
        // 关联底部导航栏与NavController
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        dataManager = LocalDataManager.getInstance();
        
        // 处理来自ProductDetailActivity的导航请求
        handleNavigationIntent(getIntent(), navController);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        handleNavigationIntent(intent, navController);
    }
    
    private void handleNavigationIntent(Intent intent, NavController navController) {
        if (intent != null && intent.getStringExtra("navigate_to") != null) {
            if (intent.getStringExtra("navigate_to").equals("chat_detail")) {
                // 跳转到聊天详情页
                com.example.myapplication.model.ChatUser chatUser = new com.example.myapplication.model.ChatUser();
                chatUser.setUserId(intent.getStringExtra("seller_id"));
                chatUser.setUserName(intent.getStringExtra("seller_name"));
                chatUser.setAvatar("");
                chatUser.setLastMessage("");
                chatUser.setLastMessageTime(String.valueOf(System.currentTimeMillis()));
                chatUser.setUnreadCount(0);
                
                Bundle bundle = new Bundle();
                bundle.putSerializable("chatUser", chatUser);
                
                navController.navigate(R.id.ChatDetailFragment, bundle);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 显示消息提示
    public void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新购物车按钮的徽章（显示商品数量）
        updateCartBadge();
    }

    private void updateCartBadge() {
        int cartItemCount = dataManager.getCartItemCount();
        // 这里可以添加购物车徽章显示逻辑
        // 比如在ActionBar上显示购物车图标和数量
    }
}