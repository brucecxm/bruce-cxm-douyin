package com.example.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.HtmlDisplayActivity;

/**
 * HTML页面工具类，用于处理HTML页面的加载和跳转
 */
public class HtmlUtils {
    private static final String TAG = "HtmlUtils";

    /**
     * 通过URL进入对应的HTML界面
     * 
     * @param context 上下文
     * @param url     要加载的URL
     */
    public static void navigateToHtmlPage(Context context, String url) {
        Log.d(TAG, "navigateToHtmlPage: 进入HTML页面，URL: " + url);

        // 创建Intent，启动HtmlDisplayActivity
        Intent intent = new Intent(context, HtmlDisplayActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * 通过本地HTML文件路径进入对应的HTML界面
     * 
     * @param context  上下文
     * @param filePath 本地HTML文件路径（相对于assets目录）
     */
    public static void navigateToLocalHtmlPage(Context context, String filePath) {
        Log.d(TAG, "navigateToLocalHtmlPage: 进入本地HTML页面，文件路径: " + filePath);

        // 构建本地URL
        String url = "http://localhost:" + 8080 + "/" + filePath;
        navigateToHtmlPage(context, url);
    }
}