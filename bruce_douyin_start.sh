#!/bin/bash

# 设置脚本执行失败时立即退出
set -e

echo "==================== 开始执行部署脚本 ===================="

# 1. **检查并拉取最新代码**
if command -v git &> /dev/null; then
    echo "git 已安装，正在从远程仓库获取最新代码..."

    # 检查是否有未提交的修改
    if ! git diff --quiet || ! git diff --staged --quiet; then
        echo "检测到未提交的本地修改，先进行 stash 处理..."
        git stash
        git pull origin main
        git stash pop
    else
        git pull origin main
    fi

    if [ $? -eq 0 ]; then
        echo "最新代码获取成功!"
    else
        echo "从远程仓库获取代码时出错!"
        exit 1
    fi
else
    echo "git 未安装，跳过更新代码步骤!"
fi

# 2. **执行 mvn install**
echo "开始执行 mvn install..."
mvn install

if [ $? -eq 0 ]; then
    echo "mvn install 执行成功!"
else
    echo "mvn install 执行失败!"
    exit 1
fi

# 3. **进入 JAR 文件夹**
cd jar || { echo "无法进入 JAR 文件夹"; exit 1; }

# 4. **检查并启动 Redis**
echo "正在检查 Redis 是否运行..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping | grep -q "PONG"; then
        echo "Redis 已经在运行!"
    else
        echo "Redis 未运行，尝试启动..."
        if systemctl list-units --type=service | grep -q "redis"; then
            sudo systemctl start redis
            if redis-cli ping | grep -q "PONG"; then
                echo "Redis 启动成功!"
            else
                echo "Redis 启动失败! 请手动检查。"
                exit 1
            fi
        else
            echo "Redis 未安装，请先安装 Redis!"
            exit 1
        fi
    fi
else
    echo "Redis 未安装，请先安装 Redis!"
    exit 1
fi

# 5. **关闭所有包含 'douyin' 和 '.jar' 的进程**
echo "正在关闭所有包含 'douyin' 和 '.jar' 字符串的进程..."
PIDS=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')

if [ -n "$PIDS" ]; then
    echo "检测到正在运行的 douyin 相关进程: $PIDS"
    echo "$PIDS" | xargs kill -9
    echo "进程已关闭!"
else
    echo "没有找到相关进程，无需关闭!"
fi

# 6. **启动所有 JAR 文件**
for jar_file in *.jar; do
    if [ -f "$jar_file" ]; then
        echo "正在启动 $jar_file ..."
        nohup java -Xms256m -Xmx512m -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
        echo "$jar_file 启动成功!"
    fi
done

echo "所有 JAR 文件已启动!"

# 返回上级目录
cd ..

# 7. **进入 Vue 项目目录**
cd douyin_vue2 || { echo "无法进入 Vue 项目文件夹"; exit 1; }

# 8. **安装 npm 依赖**
echo "开始执行 npm install..."
npm install

if [ $? -eq 0 ]; then
    echo "npm install 执行成功!"
else
    echo "npm install 执行失败!"
    exit 1
fi

# 9. **执行 Vue 项目构建**
echo "开始执行 npm run build..."
npm run build

if [ $? -eq 0 ]; then
    echo "npm build 执行成功!"
else
    echo "npm build 执行失败!"
    exit 1
fi

# 10. **重启 Nginx**
echo "正在重启 Nginx..."
sudo systemctl restart nginx

if [ $? -eq 0 ]; then
    echo "Nginx 重启成功!"
else
    echo "Nginx 重启失败!"
    exit 1
fi

echo "==================== 部署脚本执行完成! ===================="
