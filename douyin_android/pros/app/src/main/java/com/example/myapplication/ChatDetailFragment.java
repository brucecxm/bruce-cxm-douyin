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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ChatMessageAdapter;
import com.example.myapplication.model.ChatMessage;
import com.example.myapplication.model.ChatUser;
import com.example.myapplication.utils.WebSocketManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * 聊天详情页面
 */
public class ChatDetailFragment extends Fragment {
    private static final String TAG = "ChatDetailFragment";
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TOKEN = "token";

    private RecyclerView recyclerViewMessages;
    private TextInputEditText editTextMessage;
    private MaterialButton buttonSend;
    private ChatMessageAdapter adapter;
    private WebSocketManager webSocketManager;
    private String currentUserId;
    private String token;
    private ChatUser currentChatUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化WebSocket管理器
        webSocketManager = WebSocketManager.getInstance();
        
        // 获取传递过来的聊天用户
        if (getArguments() != null) {
            currentChatUser = (ChatUser) getArguments().getSerializable("chatUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 初始化视图
        initViews(view);

        // 从SharedPreferences获取用户信息
        getCurrentUserInfo();

        // 初始化适配器
        initAdapter();

        // 建立WebSocket连接
        connectWebSocket();

        // 设置发送按钮点击事件
        setButtonListeners();

        return view;
    }

    private void initViews(View view) {
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSend = view.findViewById(R.id.buttonSend);
    }

    private void getCurrentUserInfo() {
        if (isAdded()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
                    android.content.Context.MODE_PRIVATE);
            currentUserId = sharedPreferences.getString(KEY_USER_ID, "");
            token = sharedPreferences.getString(KEY_TOKEN, "");

            if (currentUserId.isEmpty() || token.isEmpty()) {
                Log.e(TAG, "用户未登录，无法使用聊天功能");
                Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initAdapter() {
        // 使用当前用户ID初始化适配器
        adapter = new ChatMessageAdapter(currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMessages.setAdapter(adapter);
        
        // 添加静态聊天消息
        addStaticChatMessages();
    }
    
    private void addStaticChatMessages() {
        // 设置默认用户ID（如果未登录）
        if (currentUserId.isEmpty()) {
            currentUserId = "2";
        }
        
        // 清空之前的消息
        adapter = new ChatMessageAdapter(currentUserId);
        recyclerViewMessages.setAdapter(adapter);
        
        String receiverId = "1"; // 默认接收者ID
        String userName = "客服小助手"; // 默认聊天对象名称
        
        // 如果有传递过来的聊天用户，则使用该用户信息
        if (currentChatUser != null) {
            receiverId = currentChatUser.getUserId();
            userName = currentChatUser.getUserName();
        }
        
        // 根据不同的聊天对象显示不同的聊天消息
        if (receiverId.equals("1")) {
            // 客服聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你好！欢迎使用社区购物平台", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "你好！我想了解一下你们的同城服务", "text"));
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "当然可以！我们的同城服务可以让你浏览附近的二手商品", "text"));
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你可以在首页点击'同城'标签查看附近的商品", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "好的，我知道了。谢谢！", "text"));
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "不客气！如果有任何问题，随时可以联系我", "text"));
        } else if (receiverId.equals("2")) {
            // 张三聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你好，这个商品还能再便宜点吗？", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "您好，这个价格已经是最低了，不能再便宜了", "text"));
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "那好吧，我买了。什么时候能发货？", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "今天下午就可以发货，预计明天就能收到", "text"));
        } else if (receiverId.equals("3")) {
            // 李四聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你好，我想了解一下这个商品的详细信息", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "您好，请问您想了解哪些方面的信息呢？", "text"));
        } else if (receiverId.equals("4")) {
            // 王五聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "商品已经收到了，谢谢！", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "不客气，祝您使用愉快！", "text"));
        } else if (receiverId.equals("5")) {
            // 赵六聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你好，这个商品还在吗？", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "您好，这个商品还在的", "text"));
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "那我现在可以拍吗？", "text"));
        } else if (receiverId.equals("6")) {
            // 孙七聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "什么时候可以发货？", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "您好，我们一般在付款后24小时内发货", "text"));
        } else {
            // 默认聊天
            adapter.addMessage(new ChatMessage(receiverId, currentUserId, "你好！", "text"));
            adapter.addMessage(new ChatMessage(currentUserId, receiverId, "你好！", "text"));
        }
        
        // 滚动到底部
        recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void connectWebSocket() {
        if (currentUserId.isEmpty() || token.isEmpty()) {
            return;
        }

        webSocketManager.connect(token, new WebSocketManager.WebSocketCallback() {
            @Override
            public void onConnected() {
                Log.d(TAG, "WebSocket连接成功");
                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null) {
                                Toast.makeText(activity, "WebSocket连接成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onDisconnected() {
                Log.d(TAG, "WebSocket连接断开");
                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null) {
                                Toast.makeText(activity, "WebSocket连接断开", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onMessageReceived(final ChatMessage message) {
                Log.d(TAG, "收到消息: " + message.toString());
                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null) {
                                // 添加消息到适配器
                                adapter.addMessage(message);
                                // 滚动到底部
                                recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(final String error) {
                Log.e(TAG, "WebSocket错误: " + error);
                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity != null) {
                                Toast.makeText(activity, "WebSocket连接失败: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void setButtonListeners() {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (content.isEmpty()) {
            if (isAdded()) {
                Toast.makeText(getActivity(), "消息内容不能为空", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (!webSocketManager.isConnected()) {
            if (isAdded()) {
                Toast.makeText(getActivity(), "WebSocket未连接，请稍后重试", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // 创建聊天消息
        ChatMessage message = new ChatMessage();
        message.setSender(currentUserId);
        
        // 根据聊天对象动态设置接收者ID
        String receiverId = "1"; // 默认接收者ID
        if (currentChatUser != null) {
            receiverId = currentChatUser.getUserId();
        }
        message.setReceiver(receiverId);
        
        message.setContent(content);
        message.setType("text");

        // 发送消息
        webSocketManager.sendMessage(message);

        // 清空输入框
        editTextMessage.setText("");

        // 将发送的消息添加到适配器
        adapter.addMessage(message);
        recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 断开WebSocket连接
        webSocketManager.disconnect();
    }
}