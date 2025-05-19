#!/bin/bash

set -e
echo "==================== 开始执行后端部署脚本 ===================="

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

# 2. 安装基础模块
echo "➡️ 安装基础模块 douyin_basic ..."
cd douyin_basic || { echo "❌ 进入 douyin_basic 失败"; exit 1; }
mvn install || { echo "❌ douyin_basic 安装失败"; exit 1; }
echo "✅ douyin_basic 安装成功"
cd ..

# 3. 安装整个项目
echo "➡️ 执行 mvn install ..."
mvn install || { echo "❌ mvn install 执行失败"; exit 1; }
echo "✅ mvn install 成功"

# 4. 启动 Redis
echo "➡️ 检查 Redis 是否运行中..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping | grep -q "PONG"; then
        echo "✅ Redis 已运行"
    else
        echo "🔄 Redis 未运行，尝试启动..."
        if systemctl list-units --type=service | grep -q "redis"; then
            sudo systemctl start redis
            redis-cli ping | grep -q "PONG" && echo "✅ Redis 启动成功" || { echo "❌ Redis 启动失败"; exit 1; }
        else
            echo "❌ 系统中未安装 Redis"
            exit 1
        fi
    fi
else
    echo "❌ 未安装 redis-cli"
    exit 1
fi

# 5. 启动 JAR 服务
cd jar || { echo "❌ 进入 jar 目录失败"; exit 1; }

echo "➡️ 杀死已有 jar 进程..."
PIDS=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')
[ -n "$PIDS" ] && echo "$PIDS" | xargs kill -9 && echo "✅ 旧进程已关闭" || echo "无旧进程"

jar_files=(*.jar)
[ ${#jar_files[@]} -eq 0 ] && echo "❌ 没有 jar 文件" && exit 1

echo ""
echo "可用的 JAR 文件列表："
for i in "${!jar_files[@]}"; do
    echo "[$i] ${jar_files[$i]}"
done

echo ""
read -p "请输入要启动的 JAR 文件编号（空格分隔）: " -a selected_indexes

for index in "${selected_indexes[@]}"; do
    if [[ "$index" =~ ^[0-9]+$ ]] && [ "$index" -ge 0 ] && [ "$index" -lt "${#jar_files[@]}" ]; then
        jar_file="${jar_files[$index]}"
        echo "🚀 启动 $jar_file ..."
        nohup java -Xms256m -Xmx512m -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
        echo "✅ $jar_file 启动成功"
    else
        echo "❌ 无效编号: $index"
    fi
done

cd ..
echo "==================== ✅ 后端部署完成 ===================="
