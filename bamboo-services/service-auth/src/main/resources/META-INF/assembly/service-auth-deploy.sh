#!/bin/bash
source /etc/profile
# shellcheck disable=SC2006
pid=`ps -ef|grep service-auth-demo.jar| grep -v grep | awk '{print $2}'`
echo "部署前的pid进程 :$pid"
# 关闭已经启动的jar进程
if [ -n "$pid" ]
  then
    kill -9 $pid
else
    echo "进程没有启动"
fi
sleep 5s

# 检查目标目录是否存在，如果不存在则创建
TARGET_DIR="/home/service-auth"
if [ ! -d "$TARGET_DIR" ]; then
    echo "目录 $TARGET_DIR 不存在，正在创建..."
    mkdir -p "$TARGET_DIR"
fi

# 复制jar包到启动目录
\cp -rf /root/.jenkins/workspace/bamboo-service-auth-demo/bamboo-services/service-auth/target/service-auth-demo.jar /home/service-auth/service-auth-demo.jar

# shellcheck disable=SC2164
cd /home/service-auth
nohup java -jar /home/service-auth/service-auth-demo.jar --spring.profiles.active=demo > /home/service-auth/nohup.out 2>&1 &
echo "脚本执行完毕"
sleep 5s
# shellcheck disable=SC2006
pid=`ps -ef|grep service-auth-demo.jar | grep -v grep | awk '{print $2}'`
# 检验进程是否启动
if [ -n "$pid" ]
  then
        echo "部署后的pid进程 :$pid"
                echo "启动成功"
else
    echo "进程没有启动"
fi