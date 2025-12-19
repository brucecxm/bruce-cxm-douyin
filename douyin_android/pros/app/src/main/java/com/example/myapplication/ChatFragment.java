package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ChatUserAdapter;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.model.ChatUser;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天用户列表
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TOKEN = "token";

    private RecyclerView recyclerViewUsers;
    private ChatUserAdapter adapter;
    private List<ChatUser> chatUserList;
    private String currentUserId;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatUserList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        // 初始化视图
        initViews(view);

        // 从SharedPreferences获取用户信息
        getCurrentUserInfo();

        // 初始化适配器
        initAdapter();

        // 从后台获取用户列表
        fetchUserList();

        return view;
    }

    private void initViews(View view) {
        recyclerViewUsers = view.findViewById(R.id.recyclerViewChatUsers);
    }

    private void getCurrentUserInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
                android.content.Context.MODE_PRIVATE);
        currentUserId = sharedPreferences.getString(KEY_USER_ID, "");
        token = sharedPreferences.getString(KEY_TOKEN, "");

        if (currentUserId.isEmpty() || token.isEmpty()) {
            Log.e(TAG, "用户未登录，无法使用聊天功能");
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void initAdapter() {
        // 初始化聊天用户适配器
        adapter = new ChatUserAdapter(getActivity(), chatUserList);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewUsers.setAdapter(adapter);

        // 设置用户点击事件
        adapter.setOnItemClickListener(new ChatUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatUser chatUser) {
                // 跳转到聊天详情页面
                Bundle bundle = new Bundle();
                bundle.putSerializable("chatUser", chatUser);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_ChatFragment_to_ChatDetailFragment, bundle);
            }
        });
    }

    private void fetchUserList() {
        // 从后台获取用户列表，这里使用OkHttpManager发送GET请求
        Map<String, String> params = new HashMap<>();
        // 可以添加分页参数
        params.put("page", "1");
        params.put("pageSize", "10");

        OkHttpManager.get("/user/list", params, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (response.isSuccess()) {
                    // 后台返回的是包含list的Map，需要正确解析
                    List<ChatUser> chatUsers = new ArrayList<>();
                    try {
                        // 将response.getData()转换为Map
                        Map<String, Object> dataMap = (Map<String, Object>) response.getData();
                        // 从Map中获取list字段
                        List<Map<String, Object>> userList = (List<Map<String, Object>>) dataMap.get("list");
                        
                        if (userList != null) {
                            // 将每个Map转换为User对象，再转换为ChatUser
                            for (Map<String, Object> userMap : userList) {
                                User user = new User();
                                user.setId((String) userMap.get("id"));
                                user.setUsername((String) userMap.get("username"));
                                user.setNickname((String) userMap.get("nickname"));
                                user.setAvatar((String) userMap.get("avatar"));
                                
                                ChatUser chatUser = new ChatUser();
                                chatUser.setUserId(user.getId());
                                chatUser.setUserName(user.getUsername());
                                chatUser.setAvatar(user.getAvatar());
                                chatUser.setLastMessage("你好，最近有什么新商品吗？");
                                chatUser.setLastMessageTime("12:00");
                                chatUser.setUnreadCount((int) (Math.random() * 10));
                                chatUsers.add(chatUser);
                            }
                        }
                        
                        // 更新UI
                        final FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity != null) {
                                        if (chatUsers.isEmpty()) {
                                            // 如果没有数据，使用模拟数据
                                            addMockUsers();
                                        } else {
                                            adapter.addUsers(chatUsers);
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "解析用户列表失败: " + e.getMessage());
                        final FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity != null) {
                                        Toast.makeText(activity, "解析用户列表失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        // 如果解析失败，使用模拟数据
                                        addMockUsers();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.e(TAG, "获取用户列表失败: " + response.getMessage());
                    final FragmentActivity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (activity != null) {
                                    Toast.makeText(activity, "获取用户列表失败: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                                    // 如果后台请求失败，使用模拟数据
                                    addMockUsers();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "网络请求失败: " + error);
                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null) {
                                Toast.makeText(activity, "获取用户列表失败: " + error, Toast.LENGTH_SHORT).show();
                                // 如果网络请求失败，使用模拟数据
                                addMockUsers();
                            }
                        }
                    });
                }
            }
        });
    }

    // 添加模拟用户数据
    private void addMockUsers() {
        List<ChatUser> mockUsers = new ArrayList<>();
        mockUsers.add(new ChatUser("1", "客服小助手"));
        mockUsers.add(new ChatUser("2", "张三"));
        mockUsers.add(new ChatUser("3", "李四"));
        mockUsers.add(new ChatUser("4", "王五"));
        mockUsers.add(new ChatUser("5", "赵六"));
        mockUsers.add(new ChatUser("6", "孙七"));

        // 设置模拟数据
        for (ChatUser user : mockUsers) {
            user.setAvatar("https://via.placeholder.com/150x150?text=User+Avatar");
            user.setLastMessage("你好，最近有什么新商品吗？");
            user.setLastMessageTime("12:00");
            user.setUnreadCount((int) (Math.random() * 10));
        }

        adapter.addUsers(mockUsers);
    }
}