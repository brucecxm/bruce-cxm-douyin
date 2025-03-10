#!/bin/bash

# 1. 执行 mvn install
echo "开始执行 mvn install..."
mvn install

# 检查 mvn install 是否执行成功
if [ $? -eq 0 ]; then
    echo "mvn install 执行成功!"
else
    echo "mvn install 执行失败!"
    exit 1
fi

# 2. 进入 JAR 文件夹
cd jar || { echo "无法进入 JAR 文件夹"; exit 1; }

# 3. 关闭所有包含 'douyin' 和 '.jar' 的进程
echo "正在关闭所有包含 'douyin' 和 '.jar' 字符串的进程..."

# 获取匹配的进程 PID
pids=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')

# 检查是否有匹配的进程
if [ -z "$pids" ]; then
    echo "没有找到匹配的进程，无需关闭！"
else
    # 如果有进程，执行 kill
    echo "找到匹配进程，正在关闭..."
    echo "$pids" | xargs kill -9
    if [ $? -eq 0 ]; then
        echo "所有匹配的进程已成功关闭！"
    else
        echo "关闭进程时出错！"
        exit 1
    fi
fi

# 4. 对每个 JAR 文件执行 nohup java -jar
for jar_file in *.jar; do
    if [ -f "$jar_file" ]; then
        echo "正在启动 $jar_file ..."
        nohup java -Xms256m -Xmx512m -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
    fi
done

echo "所有 JAR 文件已启动。"

# 返回上级目录
cd ..

# 5. 进入 Vue 项目并执行构建
cd douyin_vue2 || { echo "无法进入 Vue 项目文件夹"; exit 1; }

echo "开始执行 npm install..."
npm install  # 修正拼写错误

# 检查 npm install 是否成功
if [ $? -eq 0 ]; then
    echo "npm install 执行成功!"
else
    echo "npm install 执行失败!"
    exit 1
fi

echo "开始执行 npm run build..."
npm run build

# 检查 npm build 是否成功
if [ $? -eq 0 ]; then
    echo "npm build 执行成功!"
else
    echo "npm build 执行失败!"
    exit 1
fi

# 6. 重启 Nginx
echo "正在重启 Nginx..."
sudo systemctl restart nginx

# 检查 Nginx 是否重启成功
if [ $? -eq 0 ]; then
    echo "Nginx 重启成功!"
else
    echo "Nginx 重启失败!"
    exit 1
fi

echo "脚本执行完成。"
