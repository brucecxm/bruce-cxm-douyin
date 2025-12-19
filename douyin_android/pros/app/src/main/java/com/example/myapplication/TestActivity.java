package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d(TAG, "onCreate: 活动创建");
        
        // 初始化视图和数据
        initView();
        initData();
    }

    private void initView() {
        // 初始化视图组件
        Log.d(TAG, "initView: 初始化视图");
    }

    private void initData() {
        // 初始化数据
        Log.d(TAG, "initData: 初始化数据");
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
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: 保存实例状态");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: 恢复实例状态");
    }
}
