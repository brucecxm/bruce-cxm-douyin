#!/bin/bash

# 移除set -e以确保脚本即使出错也能继续执行

# 设置trap捕获退出信号，确保脚本退出前设置所有相关脚本的权限
trap 'ensure_scripts_permissions' EXIT

# ===================== SkyWalking 配置（可根据实际情况调整）=====================
SKYWALKING_AGENT_PATH="/home/linshi/skywalking-agent/skywalking-agent.jar"  # Agent绝对路径
SKYWALKING_BACKEND="127.0.0.1:11800"                                       # OAP地址
SKYWALKING_SERVICE_PREFIX="douyin-service"                                  # 服务名前缀（区分不同JAR）
# ==============================================================================

# 确保所有脚本权限的函数
ensure_scripts_permissions() {
    echo "🔒 正在设置所有脚本的执行权限..."
    chmod +777 "$0" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_backend_stop.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_frontend_start.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_frontend_stop.sh" 2>/dev/null || true
    chmod +777 "$(dirname "$0")/bruce_douyin_start.sh" 2>/dev/null || true
    echo "✅ 所有脚本权限设置完成"
}

# 校验SkyWalking Agent是否存在
check_skywalking_agent() {
    echo "🔍 校验SkyWalking Agent..."
    if [ ! -f "$SKYWALKING_AGENT_PATH" ]; then
        echo "❌ SkyWalking Agent不存在: $SKYWALKING_AGENT_PATH"
        echo "请确认Agent路径是否正确，或先部署SkyWalking Agent"
        exit 1
    fi
    echo "✅ SkyWalking Agent校验通过"
}

echo "==================== 开始执行后端部署脚本 ===================="

# 0. 检查 git 是否安装
if ! command -v git &> /dev/null; then
    echo "❌ git 未安装，无法拉取代码，退出"
    exit 1
fi

# 0.5 校验SkyWalking Agent（新增）
check_skywalking_agent

# 1. 选择部署环境
echo "请选择部署环境："
echo "[1] 开发环境 (dev)"
echo "[2] 生产环境 (prod)"
read -p "请输入环境编号 [1-2]: " env_choice

case "$env_choice" in
    1)
        PROFILE="dev"
        BRANCH="dev"
        JVM_OPTS="-Xms256m -Xmx512m"
        AUTO_RESTART="no"  # 开发环境默认不自动重启
        ;;
    2)
        PROFILE="prod"
        BRANCH="prod"
        JVM_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

        # 新增：生产环境是否启用自动拉取
        echo "请选择生产环境自动拉取策略："
        echo "[1] 启用自动拉取（进程退出/服务器重启后自动重启）"
        echo "[2] 禁用自动拉取（仅手动启动）"
        read -p "请输入策略编号 [1-2]: " auto_choice

        case "$auto_choice" in
            1)
                AUTO_RESTART="yes"
                ;;
            2)
                AUTO_RESTART="no"
                ;;
            *)
                echo "❌ 无效选择，默认禁用自动拉取"
                AUTO_RESTART="no"
                ;;
        esac
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

echo "🚀 部署环境：$PROFILE"
echo "🌿 拉取分支：$BRANCH"
echo "⚙️ JVM 参数：$JVM_OPTS"
echo "🔄 自动拉取：$(if [ "$AUTO_RESTART" = "yes" ]; then echo "启用"; else echo "禁用"; fi)"
echo "📊 SkyWalking：已集成（后端地址：$SKYWALKING_BACKEND）"  # 新增SkyWalking提示

# 2. 拉取指定分支的最新代码
echo "正在拉取远程分支 $BRANCH ..."
git reset --hard HEAD
git clean -fd
git fetch origin
git reset --hard "origin/$BRANCH"
echo "✅ 拉取并切换到 $BRANCH 分支完成"

# 3. 安装基础模块
echo "➡️ 安装基础模块 douyin_basic ..."
cd douyin_basic || { echo "❌ 进入 douyin_basic 失败"; exit 1; }
mvn install || { echo "❌ douyin_basic 安装失败"; exit 1; }
echo "✅ douyin_basic 安装成功"
cd ..

# 4. 安装整个项目
echo "➡️ 执行 mvn install ..."
mvn install || { echo "❌ mvn install 执行失败"; exit 1; }
echo "✅ mvn install 成功"

# 5. 启动 Redis
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

# 6. 启动 JAR 服务
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
        # 生成SkyWalking服务名（前缀+JAR名，去.jar后缀）
        service_name="${SKYWALKING_SERVICE_PREFIX}-${jar_file%.jar}"
        # 整合SkyWalking JVM参数（核心修改）
        SKYWALKING_OPTS="-javaagent:$SKYWALKING_AGENT_PATH -Dskywalking.agent.service_name=$service_name -Dskywalking.collector.backend_service=$SKYWALKING_BACKEND -Dskywalking.logging.level=INFO"
        FULL_JVM_OPTS="$JVM_OPTS $SKYWALKING_OPTS"

        echo "🚀 启动 $jar_file ($PROFILE:$BRANCH)..."
        echo "📊 SkyWalking服务名：$service_name"

        if [ "$PROFILE" = "prod" ]; then
            SERVICE_NAME="douyin-$jar_file"
            SERVICE_FILE="/etc/systemd/system/$SERVICE_NAME.service"

            LOG_DIR="/var/log/douyin"
            LOG_FILE="$LOG_DIR/$jar_file.log"

            sudo mkdir -p "$LOG_DIR"
            sudo chmod 755 "$LOG_DIR"

            if [ "$AUTO_RESTART" = "yes" ]; then
                RESTART_CONFIG="Restart=always\nRestartSec=3"
                ENABLE_STATUS="启用"
            else
                RESTART_CONFIG="Restart=no"
                ENABLE_STATUS="禁用"
            fi

            LOG_CONFIG="StandardOutput=append:$LOG_FILE\nStandardError=append:$LOG_FILE"

            echo "➡️ 配置 systemd 服务: $SERVICE_NAME (自动拉取: $ENABLE_STATUS)"
            sudo bash -c "cat > $SERVICE_FILE <<EOF
[Unit]
Description=Douyin Service: $jar_file
After=network.target redis.service skywalking-oap.service  # 依赖SkyWalking OAP

[Service]
User=root
WorkingDirectory=$(pwd)
ExecStart=/usr/bin/java $FULL_JVM_OPTS -jar $jar_file --spring.profiles.active=$PROFILE
$RESTART_CONFIG
$LOG_CONFIG

[Install]
WantedBy=multi-user.target
EOF"

            sudo systemctl daemon-reload
            sudo systemctl enable "$SERVICE_NAME"
            sudo systemctl restart "$SERVICE_NAME"

            echo "✅ $jar_file 已作为 systemd 服务启动（自动拉取: $ENABLE_STATUS）"
            echo "状态检查: systemctl status $SERVICE_NAME"
            echo "日志查看: tail -f $LOG_FILE"
            echo "📊 SkyWalking监控: 请访问 http://服务器IP:12800 查看服务($service_name)"
        else
            # 开发环境nohup启动（集成SkyWalking）
            nohup java $FULL_JVM_OPTS -jar "$jar_file" --spring.profiles.active="$PROFILE" > "$jar_file.log" 2>&1 &
            echo "✅ $jar_file 启动成功"
            echo "日志查看: tail -f $jar_file.log"
            echo "📊 SkyWalking监控: 请访问 http://服务器IP:12800 查看服务($service_name)"
        fi
    else
        echo "❌ 无效编号: $index"
    fi
done

cd ..
echo "==================== ✅ 后端部署完成 ($PROFILE:$BRANCH) ===================="
echo "📊 SkyWalking已集成，服务数据将上报至：$SKYWALKING_BACKEND"

# 显式调用权限设置函数，确保脚本执行完毕后设置所有脚本权限
ensure_scripts_permissions