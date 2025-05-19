#!/bin/bash

# 设置脚本执行失败时立即退出
set -e

echo "==================== 开始执行部署脚本 ===================="
# 1. **强制获取最新代码，覆盖本地所有改动**
if command -v git &> /dev/null; then
    echo "git 已安装，正在强制从远程仓库获取最新代码，覆盖本地改动..."

    # 清除本地所有未提交改动（包括未追踪文件）
    git reset --hard HEAD
    git clean -fd

    # 强制拉取远程 main 分支覆盖本地
    git fetch origin
    git reset --hard origin/main

    if [ $? -eq 0 ]; then
        echo "强制获取最新代码成功，已覆盖本地修改！"
    else
        echo "强制获取代码失败！"
        exit 1
    fi
else
    echo "git 未安装，跳过更新代码步骤!"
fi


## 2. **执行 mvn install**
#echo "开始执行 mvn install..."
#mvn install
#
#if [ $? -eq 0 ]; then
#    echo "mvn install 执行成功!"
#else
#    echo "mvn install 执行失败!"
#    exit 1
#fi
# 2. **先 install douyin_basic 模块**
echo "先安装基础模块 douyin_basic ..."
cd douyin_basic || { echo "无法进入 douyin_basic 模块目录"; exit 1; }

mvn install
if [ $? -eq 0 ]; then
    echo "douyin_basic 安装成功!"
else
    echo "douyin_basic 安装失败!"
    exit 1
fi

cd ..

# 2. **执行 mvn install**
echo "开始执行 mvn install..."
mvn install

if [ $? -eq 0 ]; then
    echo "mvn install 执行成功!"
else
    echo "mvn install 执行失败!"
    exit 1
fi
#!/bin/bash

# 进入 JAR 文件夹
cd jar || { echo "无法进入 JAR 文件夹"; exit 1; }

# 检查并启动 Redis
echo "正在检查 Redis 是否运行..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping | grep -q "PONG"; then
        echo "✅ Redis 已经在运行!"
    else
        echo "⚠️ Redis 未运行，尝试启动..."
        if systemctl list-units --type=service | grep -q "redis"; then
            sudo systemctl start redis
            if redis-cli ping | grep -q "PONG"; then
                echo "✅ Redis 启动成功!"
            else
                echo "❌ Redis 启动失败! 请手动检查。"
                exit 1
            fi
        else
            echo "❌ Redis 未安装，请先安装 Redis!"
            exit 1
        fi
    fi
else
    echo "❌ 未安装 redis-cli，请先安装 Redis!"
    exit 1
fi

# 关闭已有的 'douyin' jar 进程
echo "正在关闭所有包含 'douyin' 和 '.jar' 字符串的进程..."
PIDS=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')
if [ -n "$PIDS" ]; then
    echo "检测到正在运行的 douyin 相关进程: $PIDS"
    echo "$PIDS" | xargs kill -9
    echo "✅ 进程已关闭!"
else
    echo "🔍 没有找到相关进程，无需关闭!"
fi

# 扫描所有 JAR 文件
jar_files=(*.jar)
if [ ${#jar_files[@]} -eq 0 ]; then
    echo "❌ 当前目录下没有找到任何 .jar 文件"
    exit 1
fi

# 显示列表
echo ""
echo "可用的 JAR 文件如下："
for i in "${!jar_files[@]}"; do
    echo "[$i] ${jar_files[$i]}"
done

# 用户输入选择
echo ""
read -p "请输入要启动的 JAR 文件编号（用空格分隔，如：0 2 3）: " -a selected_indexes

# 启动选择的 JAR 文件
for index in "${selected_indexes[@]}"; do
    if [[ "$index" =~ ^[0-9]+$ ]] && [ "$index" -ge 0 ] && [ "$index" -lt "${#jar_files[@]}" ]; then
        jar_file="${jar_files[$index]}"
        echo "🚀 正在启动 $jar_file ..."
        nohup java -Xms256m -Xmx512m -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
        echo "✅ $jar_file 启动成功!"
    else
        echo "❌ 无效编号: $index"
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
