package com.example.myapplication.utils;

import com.example.myapplication.MyApplication;
import okhttp3.*;
import com.google.gson.Gson;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OkHttpManager {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // 回调接口
    public interface NetworkCallback<T> {
        void onSuccess(T response);

        void onFailure(String error);
    }

    // 从SharedPreferences获取token的辅助方法
    private static String getToken() {
        return MyApplication.getInstance().getSharedPreferences("user_prefs", MyApplication.getInstance().MODE_PRIVATE)
                .getString("token", null);
    }

    // POST请求（表单形式）
    public static <T> void postForm(String url, Map<String, String> params, Class<T> responseType,
            NetworkCallback<T> callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody body = formBuilder.build();
        String fullUrl = ApiConstants.BASE_URL + url;

        // 构建请求，添加token到请求头
        Request.Builder requestBuilder = new Request.Builder()
                .url(fullUrl)
                .post(body);

        // 添加token到请求头
        String token = getToken();
        if (token != null) {
            requestBuilder.addHeader("token", token);
        }

        Request request = requestBuilder.build();

        System.out.println("发送POST请求: " + fullUrl);
        System.out.println("请求参数: " + params);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败: " + e.getMessage());
                // 在主线程中执行回调
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("网络请求失败: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("请求成功，响应码: " + response.code());
                String responseBody = response.body().string();
                System.out.println("响应内容: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        T result = gson.fromJson(responseBody, responseType);
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("JSON解析失败: " + e.getMessage());
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("数据解析失败: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    // 在主线程中执行回调
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("请求失败: " + response.code() + "，响应内容: " + responseBody);
                        }
                    });
                }
            }
        });
    }

    // POST请求（JSON形式）
    public static <T> void postJson(String url, Object requestBody, Class<T> responseType,
            NetworkCallback<T> callback) {
        String jsonBody = gson.toJson(requestBody);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8"));

        // 构建请求，添加token到请求头
        Request.Builder requestBuilder = new Request.Builder()
                .url(ApiConstants.BASE_URL + url)
                .post(body);

        // 添加token到请求头
        String token = getToken();
        if (token != null) {
            requestBuilder.addHeader("token", token);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 在主线程中执行回调
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // 在主线程中执行回调
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                T result = gson.fromJson(responseBody, responseType);
                                callback.onSuccess(result);
                            } catch (Exception e) {
                                callback.onFailure("数据解析失败: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    // 在主线程中执行回调
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("请求失败: " + response.code());
                        }
                    });
                }
            }
        });
    }

    // GET请求
    public static <T> void get(String url, Map<String, String> params, Class<T> responseType,
            NetworkCallback<T> callback) {
        String fullUrl = ApiConstants.BASE_URL + url;
        if (params != null && !params.isEmpty()) {
            // 构建查询参数
            StringBuilder queryBuilder = new StringBuilder("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            // 移除最后一个&字符
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            fullUrl += queryBuilder.toString();
        }

        System.out.println("发送GET请求: " + fullUrl);

        // 构建请求，添加token到请求头
        Request.Builder requestBuilder = new Request.Builder()
                .url(fullUrl)
                .get();

        // 添加token到请求头
        String token = getToken();
        if (token != null) {
            requestBuilder.addHeader("token", token);
            System.out.println("添加token到请求头: " + token);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("GET请求失败: " + e.getMessage());
                // 在主线程中执行回调
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("GET请求成功，响应码: " + response.code());
                String responseBody = response.body().string();
                System.out.println("GET响应内容: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        T result = gson.fromJson(responseBody, responseType);
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("JSON解析失败: " + e.getMessage());
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("数据解析失败: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    // 在主线程中执行回调
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("请求失败: " + response.code() + "，响应内容: " + responseBody);
                        }
                    });
                }
            }
        });
    }

    // 文件上传请求
    public static <T> void uploadFile(String url, File file, Class<T> responseType, NetworkCallback<T> callback) {
        String fullUrl = ApiConstants.BASE_URL + url;
        System.out.println("发送文件上传请求: " + fullUrl);

        // 创建Multipart请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file));

        RequestBody requestBody = builder.build();

        // 构建请求，添加token到请求头
        Request.Builder requestBuilder = new Request.Builder()
                .url(fullUrl)
                .post(requestBody);

        // 添加token到请求头
        String token = getToken();
        if (token != null) {
            requestBuilder.addHeader("token", token);
            System.out.println("添加token到请求头: " + token);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("文件上传失败: " + e.getMessage());
                // 在主线程中执行回调
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("文件上传失败: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("文件上传请求成功，响应码: " + response.code());
                String responseBody = response.body().string();
                System.out.println("文件上传响应内容: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        T result = gson.fromJson(responseBody, responseType);
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(result);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("JSON解析失败: " + e.getMessage());
                        // 在主线程中执行回调
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("数据解析失败: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    // 在主线程中执行回调
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("请求失败: " + response.code() + "，响应内容: " + responseBody);
                        }
                    });
                }
            }
        });
    }
}
