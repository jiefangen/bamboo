#!/bin/bash
source /etc/profile

# 检查是否传递了正确的参数
if [ $# -ne 3 ]; then
  echo "错误：必须提供三个参数"
  echo "用法: $0 <目标目录> <JAR包名称> <源JAR文件路径>"
  exit 1
fi

# 从命令行参数中获取变量值
TARGET_DIR="$1"             # 启动目录
JAR_NAME="$2"               # JAR 包名称
SOURCE_JAR_PATH="/root/.jenkins/workspace$3/target/$2"        # 源 JAR 文件路径

# 检查目标目录是否存在
if [ ! -d "$TARGET_DIR" ]; then
  echo "目录 $TARGET_DIR 不存在，正在创建..."
  mkdir -p "$TARGET_DIR"
fi

# 获取进程ID
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
echo "部署前的pid进程: $pid"

# 关闭已经启动的jar进程
if [ -n "$pid" ]; then
  kill -9 $pid
else
  echo "进程没有启动"
fi
sleep 5s

# 复制jar包到目标目录
\cp -rf "$SOURCE_JAR_PATH" "$TARGET_DIR/$JAR_NAME"

# 进入目标目录
cd "$TARGET_DIR"
nohup java -jar "$TARGET_DIR/$JAR_NAME" --spring.profiles.active=demo > "$TARGET_DIR/nohup.out" 2>&1 &
echo "脚本执行完毕"
sleep 5s

# 检查进程是否启动
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
  echo "部署后的pid进程: $pid"
  echo "启动成功"
else
  echo "进程没有启动"
fi
