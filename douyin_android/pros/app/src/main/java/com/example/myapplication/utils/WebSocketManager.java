package com.example.myapplication.utils;

import android.util.Log;

import com.example.myapplication.model.ChatMessage;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {
    private static final String TAG = "WebSocketManager";
    private static final String BASE_WS_URL = "ws://10.7.27.97:7456/ws";
    private WebSocket webSocket;
    private WebSocketListener webSocketListener;
    private Gson gson = new Gson();
    private static WebSocketManager instance;
    private boolean isConnected = false;

    private WebSocketManager() {
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public interface WebSocketCallback {
        void onConnected();
        void onDisconnected();
        void onMessageReceived(ChatMessage message);
        void onError(String error);
    }

    public void connect(String token, final WebSocketCallback callback) {
        String wsUrl = BASE_WS_URL + "?token=" + token;
        OkHttpClient client = new OkHttpClient();

        try {
            URI uri = new URI(wsUrl);
            Request request = new Request.Builder().url(uri.toString()).build();

            webSocketListener = new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    Log.d(TAG, "WebSocket连接成功");
                    isConnected = true;
                    callback.onConnected();
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    Log.d(TAG, "收到WebSocket消息: " + text);
                    try {
                        ChatMessage chatMessage = gson.fromJson(text, ChatMessage.class);
                        callback.onMessageReceived(chatMessage);
                    } catch (Exception e) {
                        Log.e(TAG, "解析消息失败: " + e.getMessage());
                        callback.onError("解析消息失败: " + e.getMessage());
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    Log.d(TAG, "收到WebSocket二进制消息: " + bytes.hex());
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    Log.d(TAG, "WebSocket正在关闭: " + reason);
                    isConnected = false;
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    Log.d(TAG, "WebSocket已关闭: " + reason);
                    isConnected = false;
                    callback.onDisconnected();
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    Log.e(TAG, "WebSocket连接失败: " + t.getMessage());
                    isConnected = false;
                    callback.onError("连接失败: " + t.getMessage());
                }
            };

            webSocket = client.newWebSocket(request, webSocketListener);
        } catch (URISyntaxException e) {
            Log.e(TAG, "WebSocket连接失败: " + e.getMessage());
            callback.onError("连接失败: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void sendMessage(ChatMessage message) {
        if (webSocket != null && isConnected) {
            String messageJson = gson.toJson(message);
            webSocket.send(messageJson);
            Log.d(TAG, "发送WebSocket消息: " + messageJson);
        } else {
            Log.e(TAG, "WebSocket未连接，无法发送消息");
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "正常关闭");
            webSocket = null;
            webSocketListener = null;
            isConnected = false;
            Log.d(TAG, "WebSocket已断开连接");
        }
    }
}