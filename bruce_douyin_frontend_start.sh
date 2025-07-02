#!/bin/bash

# 1. 获取最新代码
if command -v git &> /dev/null; then
    echo "正在获取最新代码..."
    git reset --hard HEAD
    git clean -fd
    git fetch origin
    git reset --hard origin/main
    echo "✅ 获取代码成功"
else
    echo "❌ git 未安装，跳过拉取代码步骤"
fi




set -e
echo "==================== 开始执行前端部署脚本 ===================="

# 1. 进入 Vue 项目目录
cd douyin_vue2 || { echo "❌ 无法进入 Vue 项目目录"; exit 1; }

# 2. 安装依赖
echo "➡️ 安装 npm 依赖..."
npm install || { echo "❌ npm install 失败"; exit 1; }
echo "✅ npm install 成功"

# 3. 构建前端
echo "➡️ 构建 Vue 项目..."
npm run build || { echo "❌ 构建失败"; exit 1; }
echo "✅ 构建成功"

# 4. 重启 Nginx
echo "🔄 正在重启 Nginx..."
sudo systemctl restart nginx && echo "✅ Nginx 重启成功" || { echo "❌ Nginx 重启失败"; exit 1; }

# 5. 设置主脚本执行权限（可选）
SCRIPT_NAME="bruce_douyin_start.sh"
if [ -f "$SCRIPT_NAME" ]; then
    chmod +x "$SCRIPT_NAME"
    echo "✅ 为 $SCRIPT_NAME 设置了执行权限"
else
    echo "⚠️ 未找到 $SCRIPT_NAME，跳过权限设置"
fi

echo "==================== ✅ 前端部署完成 ===================="
