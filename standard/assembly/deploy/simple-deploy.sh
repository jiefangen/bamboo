#!/bin/bash

JAR_NAME="business-admin-demo.jar" # JAR包名称
TARGET_DIR="/home/business-admin" # 目标目录

# 获取进程ID
pid=`ps -ef | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'`
echo "【信息】旧服务的pid进程: $pid"

# 关闭已经启动的jar进程
if [ -n "$pid" ]; then
  kill -9 $pid
else
  echo "【警告】旧服务未启动！"
fi
sleep 5s

# 进入目标目录
cd "$TARGET_DIR"
nohup java -jar "$TARGET_DIR/$JAR_NAME" > "$TARGET_DIR/nohup.out" 2>&1 &
echo "【信息】部署脚本执行完毕"
sleep 5s