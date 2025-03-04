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

# 3. 对每个 JAR 文件执行 nohup java -jar
for jar_file in *.jar; do
    if [ -f "$jar_file" ]; then
        echo "正在启动 $jar_file ..."
        nohup java -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
    fi
done

echo "所有 JAR 文件已启动。"
