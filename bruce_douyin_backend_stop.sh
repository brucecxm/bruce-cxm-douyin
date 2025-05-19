#!/bin/bash

echo "==================== 🛑 正在执行后端停止脚本 ===================="

cd jar || { echo "❌ 无法进入 jar 目录"; exit 1; }

# 查找正在运行的 douyin 相关 jar 进程
PIDS=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')

if [ -n "$PIDS" ]; then
    echo "🔍 检测到以下进程将被终止："
    ps aux | grep 'douyin.*\.jar' | grep -v 'grep'

    echo ""
    echo "⚠️ 即将终止以上进程..."
    echo "$PIDS" | xargs kill -9
    echo "✅ 已成功终止进程"
else
    echo "✅ 当前没有正在运行的 douyin JAR 进程"
fi

echo "==================== ✅ 后端服务已关闭 ===================="
