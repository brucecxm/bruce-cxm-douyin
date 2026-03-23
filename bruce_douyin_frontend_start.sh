#!/bin/bash

# 错误处理
# 移除set -e以确保脚本即使出错也能继续执行

# 设置trap捕获退出信号，确保脚本退出前设置所有相关脚本的权限
trap 'ensure_scripts_permissions' EXIT

# 确保所有脚本权限的函数
ensure_scripts_permissions() {
    echo "🔒 正在设置所有脚本的执行权限..."
    chmod +777 "$0" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_backend_start.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_backend_stop.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_frontend_stop.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_start.sh" 2>/dev/null || true
    echo "✅ 所有脚本权限设置完成"
}
echo "==================== 开始执行前端部署脚本 ===================="

# 0. 检查 git 是否安装
if ! command -v git &> /dev/null; then
    echo "❌ git 未安装，无法拉取代码，退出"
    exit 1
fi

# 1. 选择部署环境
echo "请选择部署环境："
echo "[1] 开发环境 (dev)"
echo "[2] 生产环境 (prod)"
read -p "请输入环境编号 [1-2]: " env_choice

case "$env_choice" in
    1)
        PROFILE="dev"
        BRANCH="dev"  # 开发环境对应dev分支
        ;;
    2)
        PROFILE="prod"
        BRANCH="prod"  # 生产环境对应main分支
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

echo "🚀 部署环境：$PROFILE"
echo "🌿 拉取分支：$BRANCH"

# 2. 获取对应环境的最新代码
echo "正在拉取远程分支 $BRANCH ..."
git reset --hard HEAD
git clean -fd
git fetch origin
git reset --hard "origin/$BRANCH"
echo "✅ 拉取并切换到 $BRANCH 分支完成"

# 3. 进入 Vue 项目目录
cd douyin_vue2 || { echo "❌ 无法进入 Vue 项目目录"; exit 1; }

# 4. 安装依赖
echo "➡️ 安装 npm 依赖..."
NODE_OPTIONS="--max-old-space-size=2048 " npm install || { echo "❌ npm install 失败"; exit 1; }
echo "✅ npm install 成功"

# 5. 根据环境变量构建前端
echo "➡️ 构建 Vue 项目 (环境: $PROFILE)..."
if [ "$PROFILE" = "prod" ]; then
    NODE_OPTIONS="--max-old-space-size=2048 " npm run build:prod || { echo "❌ 生产环境构建失败"; exit 1; }
else
    NODE_OPTIONS="--max-old-space-size=2048 " npm run build:dev || { echo "❌ 开发环境构建失败"; exit 1; }
fi
echo "✅ 构建成功"

# 6. 重启 Nginx（生产环境才需要重启）
if [ "$PROFILE" = "prod" ]; then
    echo "🔄 正在重启 Nginx..."
    sudo systemctl restart nginx && echo "✅ Nginx 重启成功" || { echo "❌ Nginx 重启失败"; exit 1; }
else
    echo "⚠️ 开发环境，跳过 Nginx 重启"
fi

# 7. 设置主脚本执行权限（可选）
SCRIPT_NAME="bruce_douyin_start.sh"
if [ -f "$SCRIPT_NAME" ]; then
    chmod +x "$SCRIPT_NAME"
    echo "✅ 为 $SCRIPT_NAME 设置了执行权限"
else
    echo "⚠️ 未找到 $SCRIPT_NAME，跳过权限设置"
fi

echo "==================== ✅ 前端部署完成 ($PROFILE:$BRANCH) ===================="

# 显式调用权限设置函数，确保脚本执行完毕后设置所有脚本权限
ensure_scripts_permissions
