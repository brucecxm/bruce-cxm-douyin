#!/bin/bash

echo "==================== 🛑 开始执行前端关闭脚本 ===================="

# 1. 检查 Nginx 是否已安装
if ! command -v nginx &> /dev/null; then
    echo "❌ 未检测到 Nginx，请确认是否安装"
    exit 1
fi

# 2. 检查 Nginx 是否正在运行
NGINX_STATUS=$(ps aux | grep nginx | grep -v grep)

if [ -n "$NGINX_STATUS" ]; then
    echo "🔍 检测到正在运行的 Nginx 进程："
    echo "$NGINX_STATUS"

    echo "➡️ 正在停止 Nginx..."
    sudo systemctl stop nginx

    # 再次确认是否停止成功
    if ! pgrep nginx > /dev/null; then
        echo "✅ Nginx 已成功停止"
    else
        echo "❌ 停止 Nginx 失败，请手动检查"
        exit 1
    fi
else
    echo "✅ Nginx 当前未运行，无需关闭"
fi

echo "==================== ✅ 前端关闭完成 ===================="
