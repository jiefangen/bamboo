#!/bin/bash

# ------------------------------
# 项目部署脚本
# 用于本地或Jenkins环境常规服务部署
# ------------------------------

SCRIPT_PREFIX="[common-deploy]" # 脚本前缀

JAR_NAME="${1:-unknown.jar}" # JAR包名称
SERVER_ENV="${2:-demo}" # 服务运行环境
TARGET_DIR=$(pwd) # 目标目录
SOURCE_JAR_PATH="$TARGET_DIR/$JAR_NAME" # 源JAR文件路径

# 输出配置信息
echo "---------------------------------"
echo "$SCRIPT_PREFIX JAR_NAME: $JAR_NAME"
echo "$SCRIPT_PREFIX SOURCE_JAR_PATH: $SOURCE_JAR_PATH"
echo "$SCRIPT_PREFIX TARGET_DIR: $TARGET_DIR"
echo "---------------------------------"

# 检查目标目录是否存在
if [ ! -d "$TARGET_DIR" ]; then
  echo "$SCRIPT_PREFIX 【提示】目录 $TARGET_DIR 不存在，正在创建..."
  mkdir -p "$TARGET_DIR"
fi

# 进入目标目录
cd "$TARGET_DIR" || { echo "错误：无法进入目录 $TARGET_DIR！"; exit 1; }

# 设置日志文件夹
LOGS_DIR="$TARGET_DIR/logs"
if [ ! -d "$LOGS_DIR" ]; then
    mkdir -p "$LOGS_DIR"
fi
STDOUT_FILE="$LOGS_DIR/stdout.log"
get_timestamp() { # 获取当前时间的函数
    date "+%Y-%m-%d %H:%M:%S"
}

# 记录脚本开始的时间
echo "------------------------------- DEPLOY SCRIPT -----------------------------------" >> "$STDOUT_FILE"
START_TIME=$(date +%s)
echo "$(get_timestamp) - INFO [$(basename $0)] execution start..." >> "$STDOUT_FILE"

# 获取进程ID
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
echo "$SCRIPT_PREFIX 【信息】旧服务的pid进程: $pid" >> "$STDOUT_FILE"

# 关闭已经启动的jar进程
if [ -n "$pid" ]; then
  kill -9 $pid
else
  echo "$SCRIPT_PREFIX 【警告】旧服务未启动！" >> "$STDOUT_FILE"
fi
sleep 5s

# 复制jar包到目标目录
\cp -rf "$SOURCE_JAR_PATH" "$TARGET_DIR/$JAR_NAME"

# 进入目标目录
cd "$TARGET_DIR"
nohup java -Xms512m -Xmx512m -jar "$JAR_NAME" --spring.profiles.active="$SERVER_ENV" > "$TARGET_DIR/nohup.out" 2>&1 &
echo "$SCRIPT_PREFIX 【信息】部署脚本执行完毕" >> "$STDOUT_FILE"
sleep 5s

# 检查进程是否启动
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
  echo "$SCRIPT_PREFIX 【信息】启动成功，新服务的pid进程: $pid" >> "$STDOUT_FILE"
else
  echo "$SCRIPT_PREFIX 【错误】新服务启动失败！！！" >> "$STDOUT_FILE"
fi

echo "---------------------------------"
echo "部署脚本执行完成！"
echo "---------------------------------"

# 记录脚本结束的时间
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))
echo "$(get_timestamp) - INFO [$(basename $0)] execution ended. Total execution time: $DURATION seconds." >> "$STDOUT_FILE"
echo "------------------------------- DEPLOY SCRIPT -----------------------------------" >> "$STDOUT_FILE"