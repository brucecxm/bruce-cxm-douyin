package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class HtmlDisplayActivity extends AppCompatActivity {

    private static final String TAG = "HtmlDisplayActivity";
    private WebView webView;
    private LocalHttpServer localHttpServer;
    private static final int SERVER_PORT = 8080;
    private LinearLayout titleBarLayout;
    
    // WebSocket相关成员变量
    private OkHttpClient wsClient;
    private WebSocket webSocket;
    private String wsUrl;
    private WebSocketListener webSocketListener;
    private String jsCallback;
    
    // WebSocket连接状态
    private boolean isWebSocketConnected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_display);
        Log.d(TAG, "onCreate: 活动创建");

        // 初始化视图和数据
        initView();
        initData();
    }

    private TextView titleTextView;

    private void initView() {
        // 初始化WebView组件
        Log.d(TAG, "initView: 初始化WebView");
        webView = findViewById(R.id.web_view);
        titleTextView = findViewById(R.id.title_text);
        titleBarLayout = findViewById(R.id.title_bar_layout);

        // 设置WebView客户端
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 在当前WebView中加载URL，而不是打开浏览器
                view.loadUrl(url);
                return true;
            }
        });

        // 配置WebView设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用JavaScript
        webSettings.setDomStorageEnabled(true); // 启用DOM存储
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕大小
        webSettings.setUseWideViewPort(true); // 支持缩放
        webSettings.setBuiltInZoomControls(true); // 启用内置缩放控件
        webSettings.setDisplayZoomControls(false); // 隐藏缩放控件

        // 注入JavaScript接口，允许Vue页面调用Android原生方法
        webView.addJavascriptInterface(new AndroidJsBridge(), "Android");
    }

    private void initData() {
        // 初始化数据，启动本地HTTP服务器并加载HTML内容
        Log.d(TAG, "initData: 启动本地HTTP服务器");

        try {
            // 创建并启动本地HTTP服务器
            localHttpServer = new LocalHttpServer();
            localHttpServer.start();
            Log.d(TAG, "initData: 本地HTTP服务器启动成功，端口: " + SERVER_PORT);

            // 获取要加载的URL，如果没有传递URL，则加载默认页面
            String url = getIntent().getStringExtra("url");
            if (url == null || url.isEmpty()) {
                // 加载默认页面
                url = "http://localhost:" + SERVER_PORT + "/login.html";
            }
            Log.d(TAG, "initData: 加载URL: " + url);
            webView.loadUrl(url);
        } catch (IOException e) {
            Log.e(TAG, "initData: 启动本地HTTP服务器失败", e);
            // 如果服务器启动失败，使用备用方案
            String htmlContent = loadHtmlFromAssets("index.html");
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
        }
    }

    /**
     * 自动生成原生页面路由配置文件
     */
    private void generateNativeRoutesConfig() {
        Log.d(TAG, "generateNativeRoutesConfig: 开始生成原生页面路由配置");

        // 要扫描的包名
        String packageName = "com.example.myapplication";

        // 要排除的Activity（如MainActivity、HtmlDisplayActivity等）
        String[] excludedActivities = {
                "MainActivity",
                "HtmlDisplayActivity"
        };

        // 创建路由配置列表
        org.json.JSONArray nativeRoutes = new org.json.JSONArray();

        try {
            // 获取应用程序的包管理器
            android.content.pm.PackageManager packageManager = getPackageManager();

            // 获取应用程序的所有Activity
            android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(),
                    android.content.pm.PackageManager.GET_ACTIVITIES);

            // 遍历所有Activity
            for (android.content.pm.ActivityInfo activityInfo : packageInfo.activities) {
                String activityName = activityInfo.name;
                String simpleName = activityName.substring(activityName.lastIndexOf(".") + 1);

                // 跳过排除的Activity
                boolean isExcluded = false;
                for (String excluded : excludedActivities) {
                    if (simpleName.equals(excluded)) {
                        isExcluded = true;
                        break;
                    }
                }
                if (isExcluded) {
                    Log.d(TAG, "generateNativeRoutesConfig: 跳过排除的Activity: " + simpleName);
                    continue;
                }

                // 生成路由路径（小写，去掉Activity后缀）
                String path = "/" + simpleName.replace("Activity", "").toLowerCase();

                // 生成标题（首字母大写，去掉Activity后缀，添加空格）
                String title = simpleName.replace("Activity", "");
                title = title.replaceAll("([a-z])([A-Z])", "$1 $2");
                title = title.substring(0, 1).toUpperCase() + title.substring(1);

                // 创建路由配置对象
                org.json.JSONObject route = new org.json.JSONObject();
                route.put("path", path);
                route.put("activity", activityName);
                route.put("title", title);

                // 添加到路由列表
                nativeRoutes.put(route);
                Log.d(TAG, "generateNativeRoutesConfig: 添加路由: " + route.toString());
            }

            // 将路由配置写入文件
            String jsonString = nativeRoutes.toString(2); // 格式化JSON，缩进2个空格

            // 获取assets目录的绝对路径
            java.io.File assetsDir = new java.io.File(getFilesDir().getParent() + "/assets");
            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }

            // 写入文件
            java.io.File nativeRoutesFile = new java.io.File(assetsDir, "native_routes.json");
            java.io.FileWriter writer = new java.io.FileWriter(nativeRoutesFile);
            writer.write(jsonString);
            writer.flush();
            writer.close();

            Log.d(TAG, "generateNativeRoutesConfig: 原生页面路由配置生成成功: " + nativeRoutesFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "generateNativeRoutesConfig: 生成原生页面路由配置失败", e);
        }
    }

    /**
     * JavaScript接口类，用于Vue页面调用Android原生方法
     */
    private class AndroidJsBridge {
        private android.widget.Toast loadingToast;

        /**
         * 显示Toast消息
         */
        @android.webkit.JavascriptInterface
        public void showToast(String message, int duration) {
            Log.d(TAG, "showToast: 收到JS调用，消息: " + message + ", 时长: " + duration);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 根据duration参数决定显示时长
                    int toastDuration = duration <= 1500 ? android.widget.Toast.LENGTH_SHORT : android.widget.Toast.LENGTH_LONG;
                    android.widget.Toast.makeText(HtmlDisplayActivity.this, message, toastDuration)
                            .show();
                }
            });
        }
        
        /**
         * 显示Toast消息（兼容旧版本，只接受一个参数）
         */
        @android.webkit.JavascriptInterface
        public void showToast(String message) {
            showToast(message, 1500); // 默认时长1500ms
        }

        /**
         * 显示加载中Toast
         */
        @android.webkit.JavascriptInterface
        public void showLoading(String message) {
            Log.d(TAG, "showLoading: 收到JS调用，消息: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (loadingToast != null) {
                        loadingToast.cancel();
                    }
                    loadingToast = android.widget.Toast.makeText(HtmlDisplayActivity.this, message,
                            android.widget.Toast.LENGTH_LONG);
                    loadingToast.show();
                }
            });
        }

        /**
         * 隐藏加载中Toast
         */
        @android.webkit.JavascriptInterface
        public void hideLoading() {
            Log.d(TAG, "hideLoading: 收到JS调用");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (loadingToast != null) {
                        loadingToast.cancel();
                        loadingToast = null;
                    }
                }
            });
        }

        /**
         * 页面跳转
         */
        @android.webkit.JavascriptInterface
        public void navigateToPage(String path) {
            Log.d(TAG, "navigateToPage: 收到JS调用，跳转路径: " + path);

            // 根据路径加载对应的HTML文件
            String url = "http://localhost:" + SERVER_PORT;
            if (path.equals("/index")) {
                url += "/index.html";
            } else if (path.equals("/page1")) {
                url += "/page1.html";
            } else if (path.equals("/page2")) {
                url += "/page2.html";
            } else if (path.equals("/login")) {
                url += "/login.html";
            } else {
                // 默认加载首页
                url += "/index.html";
            }

            final String finalUrl = url;
            Log.d(TAG, "navigateToPage: 加载URL: " + finalUrl);

            // 在主线程中执行WebView操作
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(finalUrl);
                }
            });
        }

        /**
         * 跳转到原生登录页面
         */
        @android.webkit.JavascriptInterface
        public void navigateToLoginPage() {
            Log.d(TAG, "navigateToLoginPage: 收到JS调用，跳转到登录页面");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 跳转到登录页面
                    Intent intent = new Intent(HtmlDisplayActivity.this, LoginActivity.class);
                    startActivity(intent);
                    // 可以选择是否关闭当前页面
                    // finish();
                }
            });
        }

        /**
         * 根据URL或ID跳转到原生页面
         */
        @android.webkit.JavascriptInterface
        public void navigateToNativePage(String target) {
            Log.d(TAG, "navigateToNativePage: 收到JS调用，目标: " + target);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 直接写死的原生页面路由配置
                        String nativeRoutesJson = "[\n" +
                                "  {\n" +
                                "    \"path\": \"/login\",\n" +
                                "    \"activity\": \"com.example.myapplication.LoginActivity\",\n" +
                                "    \"title\": \"登录页面\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"path\": \"/register\",\n" +
                                "    \"activity\": \"com.example.myapplication.RegisterActivity\",\n" +
                                "    \"title\": \"注册页面\"\n" +
                                "  }\n" +
                                "]";
                        Log.d(TAG, "navigateToNativePage: 原生页面路由配置: " + nativeRoutesJson);

                        // 解析JSON配置
                        org.json.JSONArray nativeRoutes = new org.json.JSONArray(nativeRoutesJson);

                        // 根据目标匹配路由
                        for (int i = 0; i < nativeRoutes.length(); i++) {
                            org.json.JSONObject route = nativeRoutes.getJSONObject(i);
                            String path = route.getString("path");
                            String activityName = route.getString("activity");

                            // 匹配目标路径或活动名称
                            // 支持匹配完整路径、路径名（如/login或login）、活动名称（不区分大小写）
                            if (path.equals(target) ||
                                    path.equals("/" + target) ||
                                    path.toLowerCase().contains(target.toLowerCase()) ||
                                    activityName.toLowerCase().contains(target.toLowerCase())) {
                                Log.d(TAG, "navigateToNativePage: 匹配到路由: " + path + "，活动: " + activityName);

                                // 动态加载并启动Activity
                                Class<?> activityClass = Class.forName(activityName);
                                Intent intent = new Intent(HtmlDisplayActivity.this, activityClass);
                                startActivity(intent);
                                return;
                            }
                        }

                        // 如果没有匹配到路由
                        Log.d(TAG, "navigateToNativePage: 未找到匹配的原生页面: " + target);
                    } catch (Exception e) {
                        Log.e(TAG, "navigateToNativePage: 跳转到原生页面失败", e);
                    }
                }
            });
        }

        /**
         * 获取路由配置
         */
        @android.webkit.JavascriptInterface
        public String getRoutes() {
            Log.d(TAG, "getRoutes: 收到JS调用，获取路由配置");
            try {
                // 从assets目录读取routes.json文件
                String routesJson = loadHtmlFromAssets("routes.json");
                Log.d(TAG, "getRoutes: 路由配置: " + routesJson);
                return routesJson;
            } catch (Exception e) {
                Log.e(TAG, "getRoutes: 读取路由配置失败", e);
                return "[]";
            }
        }

        /**
         * 通知有新版本可用
         */
        @android.webkit.JavascriptInterface
        public void notifyNewVersion(String versionInfoJson) {
            Log.d(TAG, "notifyNewVersion: 收到JS调用，版本信息: " + versionInfoJson);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 这里可以处理版本更新逻辑
                    // 例如：显示更新提示对话框，或者后台下载更新
                    android.widget.Toast
                            .makeText(HtmlDisplayActivity.this, "发现新版本，将在后台下载", android.widget.Toast.LENGTH_SHORT)
                            .show();

                    // 实际项目中，可以在这里启动下载服务
                    // startDownloadService(versionInfoJson);
                }
            });
        }

        /**
         * 获取本地版本号
         */
        @android.webkit.JavascriptInterface
        public String getLocalVersion() {
            Log.d(TAG, "getLocalVersion: 收到JS调用，获取本地版本号");
            // 这里可以返回本地存储的版本号
            return "v1.0.0";
        }

        /**
         * 设置页面标题
         */
        @android.webkit.JavascriptInterface
        public void setTitle(String title) {
            Log.d(TAG, "setTitle: 收到JS调用，设置标题: " + title);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleTextView.setText(title);
                }
            });
        }

        /**
         * 隐藏标题栏
         */
        @android.webkit.JavascriptInterface
        public void hideTitleBar() {
            Log.d(TAG, "hideTitleBar: 收到JS调用，隐藏标题栏");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleBarLayout.setVisibility(View.GONE);
                }
            });
        }

        /**
         * 显示标题栏
         */
        @android.webkit.JavascriptInterface
        public void showTitleBar() {
            Log.d(TAG, "showTitleBar: 收到JS调用，显示标题栏");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleBarLayout.setVisibility(View.VISIBLE);
                }
            });
        }
        
        /**
         * 连接WebSocket
         */
        @android.webkit.JavascriptInterface
        public void connectWebSocket(String url, String callbackName) {
            Log.d(TAG, "connectWebSocket: 收到JS调用，连接WebSocket: " + url + ", 回调: " + callbackName);
            HtmlDisplayActivity.this.jsCallback = callbackName;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HtmlDisplayActivity.this.connectWebSocket(url);
                }
            });
        }
        
        /**
         * 断开WebSocket连接
         */
        @android.webkit.JavascriptInterface
        public void disconnectWebSocket() {
            Log.d(TAG, "disconnectWebSocket: 收到JS调用，断开WebSocket连接");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HtmlDisplayActivity.this.disconnectWebSocket();
                }
            });
        }
        
        /**
         * 发送WebSocket消息
         */
        @android.webkit.JavascriptInterface
        public void sendWebSocketMessage(String message) {
            Log.d(TAG, "sendWebSocketMessage: 收到JS调用，发送WebSocket消息: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendWebSocketMessage(message);
                }
            });
        }
        
        /**
         * 订阅WebSocket消息
         */
        @android.webkit.JavascriptInterface
        public void subscribeWebSocket(String callbackName) {
            Log.d(TAG, "subscribeWebSocket: 收到JS调用，订阅WebSocket消息，回调: " + callbackName);
            HtmlDisplayActivity.this.jsCallback = callbackName;
        }
    }

    /**
     * 从assets目录读取HTML文件内容
     * 
     * @param fileName 文件名
     * @return HTML内容字符串
     */
    private String loadHtmlFromAssets(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // 获取AssetManager实例
            android.content.res.AssetManager assetManager = getAssets();
            // 打开文件输入流
            InputStream inputStream = assetManager.open(fileName);
            // 包装为BufferedReader
            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(inputStream, "UTF-8"));

            String line;
            // 逐行读取文件内容
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            // 关闭资源
            bufferedReader.close();
            inputStream.close();

            Log.d(TAG, "loadHtmlFromAssets: 成功读取文件: " + fileName);
        } catch (IOException e) {
            Log.e(TAG, "loadHtmlFromAssets: 读取文件失败: " + fileName, e);
            // 返回默认错误HTML
            return "<html><body><h1>读取文件失败</h1><p>无法加载HTML文件: " + e.getMessage() + "</p></body></html>";
        }

        return stringBuilder.toString();
    }

    /**
     * 本地HTTP服务器类，用于解决CORS跨域问题和实现反向代理
     */
    private class LocalHttpServer extends NanoHTTPD {
        // 代理配置：匹配/api开头的请求，转发到目标服务器
        private static final String PROXY_PREFIX = "/api";
        private static final String TARGET_SERVER = "http://1.94.131.16:7400";
        // WebSocket目标服务器（将http替换为ws）
        private static final String TARGET_WS_SERVER = TARGET_SERVER.replace("http", "ws");

        public LocalHttpServer() {
            super(SERVER_PORT);
        }

        @Override
        public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
            try {
                // 获取请求的URI
                String uri = session.getUri();
                Map<String, String> headers = session.getHeaders();
                String upgradeHeader = headers.get("upgrade");

                Log.d(TAG, "LocalHttpServer: 收到请求: " + uri + ", Upgrade: " + upgradeHeader);

                // 检查是否是WebSocket升级请求
                if (upgradeHeader != null && upgradeHeader.equalsIgnoreCase("websocket")) {
                    // 处理WebSocket反向代理
                    if (uri.startsWith(PROXY_PREFIX)) {
                        Log.d(TAG, "LocalHttpServer: 处理WebSocket反向代理请求: " + uri);
                        return handleWebSocketProxy(session, uri);
                    }
                }

                // 检查是否需要HTTP反向代理
                if (uri.startsWith(PROXY_PREFIX)) {
                    // 处理HTTP反向代理请求
                    Log.d(TAG, "LocalHttpServer: 处理HTTP反向代理请求: " + uri);
                    return handleProxyRequest(session, uri);
                }

                // 处理本地资源请求
                // 默认请求index.html
                String fileName = uri.equals("/") ? "index.html" : uri.substring(1);

                // 从assets目录读取文件
                InputStream inputStream = getAssets().open(fileName);

                // 获取文件MIME类型
                String mimeType = getMimeType(fileName);

                // 创建响应，添加CORS头
                NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType,
                        inputStream, inputStream.available());
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

                return response;
            } catch (IOException e) {
                Log.e(TAG, "LocalHttpServer: 读取文件失败", e);
                // 返回404响应
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/html",
                        "<html><body><h1>404 Not Found</h1></body></html>");
            }
        }

        /**
         * 处理WebSocket反向代理
         */
        private NanoHTTPD.Response handleWebSocketProxy(IHTTPSession session, String uri) {
            try {
                // 构建目标WebSocket URL，去掉/api前缀
                String targetUri = uri.substring(PROXY_PREFIX.length());
                String targetWsUrl = TARGET_WS_SERVER + targetUri;
                Log.d(TAG, "LocalHttpServer: WebSocket转发到: " + targetWsUrl);

                // 获取WebSocket握手请求的密钥
                String key = session.getHeaders().get("sec-websocket-key");
                String protocol = session.getHeaders().get("sec-websocket-protocol");
                String extensions = session.getHeaders().get("sec-websocket-extensions");

                // 计算WebSocket握手响应密钥
                String responseKey = generateWebSocketResponseKey(key);

                // 创建WebSocket握手响应，使用101状态码
                NanoHTTPD.Response.Status status = NanoHTTPD.Response.Status.lookup(101);
                // 使用正确的方法签名，传入状态码、内容类型和响应体
                NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse(
                        status != null ? status : NanoHTTPD.Response.Status.INTERNAL_ERROR,
                        "",
                        "");
                // 使用空字符串作为响应体，NanoHTTPD会自动处理内容长度

                // 添加WebSocket握手响应头
                response.addHeader("Upgrade", "websocket");
                response.addHeader("Connection", "Upgrade");
                response.addHeader("Sec-WebSocket-Accept", responseKey);
                if (protocol != null) {
                    response.addHeader("Sec-WebSocket-Protocol", protocol);
                }
                if (extensions != null) {
                    response.addHeader("Sec-WebSocket-Extensions", extensions);
                }
                response.addHeader("Access-Control-Allow-Origin", "*");

                Log.d(TAG, "LocalHttpServer: WebSocket握手响应已准备");
                return response;
            } catch (Exception e) {
                Log.e(TAG, "LocalHttpServer: WebSocket代理失败", e);
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR,
                        "text/plain", "WebSocket proxy failed: " + e.getMessage());
            }
        }

        /**
         * 生成WebSocket响应密钥
         */
        private String generateWebSocketResponseKey(String key) {
            try {
                String magicString = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
                String combined = key + magicString;
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] hash = md.digest(combined.getBytes(StandardCharsets.UTF_8));
                return Base64.encodeToString(hash, Base64.DEFAULT);
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "LocalHttpServer: 生成WebSocket响应密钥失败", e);
                return "";
            }
        }

        /**
         * 处理反向代理请求
         */
        private NanoHTTPD.Response handleProxyRequest(IHTTPSession session, String uri) {
            try {
                // 构建目标URL，去掉/api前缀
                String targetUri = uri.substring(PROXY_PREFIX.length());
                String targetUrl = TARGET_SERVER + targetUri;
                Log.d(TAG, "LocalHttpServer: 转发请求到: " + targetUrl);

                // 创建OkHttp客户端
                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                        .build();

                // 构建请求
                okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                        .url(targetUrl);

                // 复制请求头
                for (String headerName : session.getHeaders().keySet()) {
                    // 跳过一些不需要转发的头
                    if (!"connection".equalsIgnoreCase(headerName) &&
                            !"host".equalsIgnoreCase(headerName) &&
                            !"content-length".equalsIgnoreCase(headerName)) {
                        requestBuilder.header(headerName, session.getHeaders().get(headerName));
                    }
                }

                // 根据请求方法构建请求体
                String method = session.getMethod().name();
                okhttp3.RequestBody requestBody = null;
                if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
                    // 读取请求体
                    Map<String, String> files = new HashMap<>();
                    session.parseBody(files);
                    
                    // 获取请求体内容
                    String body = "";
                    if (files.containsKey("postData")) {
                        body = files.get("postData");
                    }

                    if (!body.isEmpty()) {
                        requestBody = okhttp3.RequestBody.create(
                                body,
                                okhttp3.MediaType.parse(session.getHeaders().get("content-type")));
                    }
                }

                // 设置请求方法和请求体
                switch (method) {
                    case "GET":
                        requestBuilder.get();
                        break;
                    case "POST":
                        requestBuilder.post(
                                requestBody != null ? requestBody : okhttp3.RequestBody.create(new byte[0], null));
                        break;
                    case "PUT":
                        requestBuilder
                                .put(requestBody != null ? requestBody : okhttp3.RequestBody.create(new byte[0], null));
                        break;
                    case "DELETE":
                        if (requestBody != null) {
                            requestBuilder.delete(requestBody);
                        } else {
                            requestBuilder.delete();
                        }
                        break;
                    case "OPTIONS":
                        requestBuilder.method("OPTIONS", null);
                        break;
                    default:
                        requestBuilder.method(method, requestBody);
                }

                // 执行请求
                okhttp3.Response okResponse = client.newCall(requestBuilder.build()).execute();

                // 处理响应
                okhttp3.ResponseBody responseBody = okResponse.body();
                String contentType = okResponse.header("content-type", "application/octet-stream");
                int contentLength = (int) (responseBody != null ? responseBody.contentLength() : 0);
                InputStream responseStream = responseBody != null ? responseBody.byteStream() : null;

                // 转换状态码
                NanoHTTPD.Response.Status status = NanoHTTPD.Response.Status.lookup(okResponse.code());
                if (status == null) {
                    status = NanoHTTPD.Response.Status.INTERNAL_ERROR;
                }

                // 创建NanoHTTPD响应
                NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse(
                        status,
                        contentType,
                        responseStream,
                        contentLength);

                // 复制响应头
                for (String headerName : okResponse.headers().names()) {
                    response.addHeader(headerName, okResponse.header(headerName));
                }

                // 添加CORS头
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

                return response;
            } catch (Exception e) {
                Log.e(TAG, "LocalHttpServer: 代理请求失败", e);
                return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR,
                        "text/plain", "Proxy request failed: " + e.getMessage());
            }
        }

        /**
         * 根据文件名获取MIME类型
         */
        private String getMimeType(String fileName) {
            if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                return "text/html";
            } else if (fileName.endsWith(".css")) {
                return "text/css";
            } else if (fileName.endsWith(".js")) {
                return "application/javascript";
            } else if (fileName.endsWith(".png")) {
                return "image/png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (fileName.endsWith(".gif")) {
                return "image/gif";
            } else if (fileName.endsWith(".json")) {
                return "application/json";
            } else {
                return "application/octet-stream";
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: 活动可见");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 活动获取焦点");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 活动失去焦点");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: 活动不可见");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 活动重新启动");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 活动销毁");

        // 停止本地HTTP服务器
        if (localHttpServer != null) {
            localHttpServer.stop();
            Log.d(TAG, "onDestroy: 本地HTTP服务器已停止");
        }

        // 关闭WebSocket连接
        if (webSocket != null) {
            webSocket.close(1000, "活动销毁");
            webSocket = null;
        }

        // 销毁WebView，释放资源
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: 保存实例状态");
        // 保存WebView状态
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: 恢复实例状态");
        // 恢复WebView状态
        webView.restoreState(savedInstanceState);
    }

    /**
     * 连接WebSocket
     */
    private void connectWebSocket(String url) {
        Log.d(TAG, "connectWebSocket: 连接WebSocket: " + url);
        this.wsUrl = url;
        
        // 初始化WebSocket客户端
        wsClient = new OkHttpClient.Builder()
                .build();
        
        // 创建WebSocket监听器
        webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "WebSocket: 连接成功");
                isWebSocketConnected = true;
                notifyJs("{\"type\":\"open\",\"message\":\"WebSocket连接成功\"}");
            }
            
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "WebSocket: 收到消息: " + text);
                notifyJs("{\"type\":\"message\",\"message\":\"" + text + "\"}");
            }
            
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d(TAG, "WebSocket: 收到二进制消息: " + bytes.hex());
                notifyJs("{\"type\":\"binaryMessage\",\"message\":\"" + bytes.hex() + "\"}");
            }
            
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket: 正在关闭连接，代码: " + code + ", 原因: " + reason);
                isWebSocketConnected = false;
                webSocket.close(1000, null);
                notifyJs("{\"type\":\"closing\",\"code\":\"" + code + ",\"reason\":\"" + reason + "\"}");
            }
            
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket: 连接已关闭，代码: " + code + ", 原因: " + reason);
                isWebSocketConnected = false;
                notifyJs("{\"type\":\"closed\",\"code\":\"" + code + ",\"reason\":\"" + reason + "\"}");
            }
            
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "WebSocket: 连接失败", t);
                isWebSocketConnected = false;
                notifyJs("{\"type\":\"error\",\"message\":\"" + t.getMessage() + "\"}");
            }
        };
        
        // 创建WebSocket请求
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        // 建立WebSocket连接
        webSocket = wsClient.newWebSocket(request, webSocketListener);
    }
    
    /**
     * 断开WebSocket连接
     */
    private void disconnectWebSocket() {
        Log.d(TAG, "disconnectWebSocket: 断开WebSocket连接");
        if (webSocket != null) {
            webSocket.close(1000, "主动关闭连接");
            webSocket = null;
        }
        isWebSocketConnected = false;
    }
    
    /**
     * 发送WebSocket消息
     */
    private void sendWebSocketMessage(String message) {
        Log.d(TAG, "sendWebSocketMessage: 发送WebSocket消息: " + message);
        if (webSocket != null && isWebSocketConnected) {
            boolean success = webSocket.send(message);
            if (success) {
                notifyJs("{\"type\":\"sendSuccess\",\"message\":\"消息发送成功\"}");
            } else {
                notifyJs("{\"type\":\"sendError\",\"message\":\"消息发送失败\"}");
            }
        } else {
            notifyJs("{\"type\":\"sendError\",\"message\":\"WebSocket未连接\"}");
        }
    }
    
    /**
     * 通知JavaScript WebSocket事件
     */
    private void notifyJs(String message) {
        Log.d(TAG, "notifyJs: 通知JavaScript: " + message);
        if (jsCallback != null && !jsCallback.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript(jsCallback + "(\"" + message + "\")", null);
                }
            });
        }
    }
    
    @Override
    public void onBackPressed() {
        // 如果WebView可以返回，则返回上一页，否则退出活动
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
