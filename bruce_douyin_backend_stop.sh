#!/bin/bash

# 设置trap捕获退出信号，确保脚本退出前设置所有相关脚本的权限
trap 'ensure_scripts_permissions' EXIT

# 确保所有脚本权限的函数
ensure_scripts_permissions() {
    echo "🔒 正在设置所有脚本的执行权限..."
    chmod +777 "$0" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_backend_start.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_frontend_start.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_frontend_stop.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_start.sh" 2>/dev/null || true
    echo "✅ 所有脚本权限设置完成"
}

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

# 显式调用权限设置函数，确保脚本执行完毕后设置所有脚本权限
ensure_scripts_permissions
