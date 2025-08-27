#!/bin/bash

# ------------------------------
# 项目部署脚本
# 用于本地或Jenkins环境常规服务部署
# ------------------------------

SCRIPT_PREFIX="[deploy]" # 脚本前缀

# 检查是否传递了正确的参数
if [ $# -ne 3 ]; then
  echo "$SCRIPT_PREFIX 错误：必须提供三个参数"
  echo "$SCRIPT_PREFIX 用法: $0 <JAR包名称> <源JAR包文件目录> <目标目录>"
  exit 1
fi

# 从命令行参数中获取变量值
JAR_NAME="${1:-unknown.jar}" # JAR包名称
SOURCE_DIR="${2:-/root}" # 源JAR包文件目录
TARGET_DIR="${3:-/home}" # 目标目录

SOURCE_JAR_PATH="$SOURCE_DIR/$JAR_NAME" # 源JAR文件路径

echo "$SCRIPT_PREFIX JAR_NAME: $JAR_NAME"
echo "$SCRIPT_PREFIX SOURCE_JAR_PATH: $SOURCE_JAR_PATH"
echo "$SCRIPT_PREFIX TARGET_DIR: $TARGET_DIR"

# 检查目标目录是否存在
if [ ! -d "$TARGET_DIR" ]; then
  echo "$SCRIPT_PREFIX 【提示】目录 $TARGET_DIR 不存在，正在创建..."
  mkdir -p "$TARGET_DIR"
fi

# 获取进程ID
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
echo "$SCRIPT_PREFIX 【信息】旧服务的pid进程: $pid"

# 关闭已经启动的jar进程
if [ -n "$pid" ]; then
  kill -9 $pid
else
  echo "$SCRIPT_PREFIX 【警告】旧服务未启动！"
fi
sleep 5s

# 复制jar包到目标目录
\cp -rf "$SOURCE_JAR_PATH" "$TARGET_DIR/$JAR_NAME"

# 进入目标目录
cd "$TARGET_DIR"
nohup java -jar "$TARGET_DIR/$JAR_NAME" > "$TARGET_DIR/nohup.out" 2>&1 &
echo "$SCRIPT_PREFIX 【信息】部署脚本执行完毕"
sleep 5s

# 检查进程是否启动
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
  echo "$SCRIPT_PREFIX 【信息】启动成功，新服务的pid进程: $pid"
else
  echo "$SCRIPT_PREFIX 【错误】新服务启动失败！！！"
fi