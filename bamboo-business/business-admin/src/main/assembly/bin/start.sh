#!/bin/bash

# 获取当前脚本所在路径，并设定部署目录、配置目录
cd "$(dirname "$0")" || exit 1
cd .. || exit 1
DEPLOY_DIR="$(pwd)"
CONF_DIR="$DEPLOY_DIR/conf"
LOGS_DIR="$DEPLOY_DIR/logs"

# 从配置文件中提取服务运行的配置信息
SERVER_NAME=$(awk -F '=' '/application.name/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
SERVER_ENV=$(awk -F '=' '/profiles.active/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
SERVER_PORT=$(awk -F '=' '/server.port/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
HEAP_SIZE_MB=$(awk -F '=' '/heap.size/ {gsub(/\r/, ""); print $2}' conf/maven.properties)

# 检查是否有为空的变量
if [ -z "$SERVER_NAME" ]; then
    echo "ERROR: SERVER_NAME (application.name) is missing or empty!"
    exit 1
fi
if [ -z "$SERVER_ENV" ]; then
    echo "ERROR: SERVER_ENV (profiles.active) is missing or empty!"
    exit 1
fi
if [ -z "$SERVER_PORT" ]; then
    echo "ERROR: SERVER_PORT (server.port) is missing or empty!"
    exit 1
fi

# 输出配置信息
echo "-------------- Configuration Information -------------------"
echo "DEPLOY_DIR: $DEPLOY_DIR"
echo "SERVER_NAME: $SERVER_NAME"
echo "SERVER_ENV: $SERVER_ENV"
echo "SERVER_PORT: $SERVER_PORT"
echo "HEAP_SIZE_MB: $HEAP_SIZE_MB"
echo "-------------- Configuration Information -------------------"

# 检查服务是否已经启动
PIDS=$(pgrep -f "$DEPLOY_DIR" | grep -v grep)
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME is already running with PID: $PIDS"
    exit 1
fi

# 检查端口是否被占用
if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=$(netstat -tln | grep -c "$SERVER_PORT")
    if [ "$SERVER_PORT_COUNT" -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT is already in use!"
        exit 1
    fi
fi

# 设置日志文件夹
if [ ! -d "$LOGS_DIR" ]; then
    mkdir -p "$LOGS_DIR" || { echo "ERROR: Failed to create log directory"; exit 1; }
fi
STDOUT_FILE="$LOGS_DIR/stdout.log"

# 配置JVM参数
JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"
# 获取项目服务配置HEAP_SIZE参数（单位：MB）
JVM_HEAP_SIZE="$HEAP_SIZE_MB"
# 判断参数是否有效（128MB到6144MB之间）最小约128MB最大约6G
if [[ -n "$JVM_HEAP_SIZE" && "$JVM_HEAP_SIZE" -ge 128 && "$JVM_HEAP_SIZE" -le 6144 ]]; then
    JVM_HEAP_SIZE="${JVM_HEAP_SIZE}m"
    echo "Using service configuration JVM_HEAP_SIZE: ${JVM_HEAP_SIZE}"
else
  # 获取宿主机器的内存大小（单位：MB）
  HOST_MEMORY=$(free -m | grep Mem | awk '{print $2}')
  # 根据宿主内存大小动态设置JVM堆大小
  if [ "$HOST_MEMORY" -le 1024 ]; then
      JVM_HEAP_SIZE="600m"
  elif [ "$HOST_MEMORY" -le 2048 ]; then
      JVM_HEAP_SIZE="1434m"
  elif [ "$HOST_MEMORY" -le 4096 ]; then
      JVM_HEAP_SIZE="2867m"
  else
      JVM_HEAP_SIZE="2867m"  # For machines with more than 4 GB of memory, use the same size as 4 GB
  fi
fi
JAVA_MEM_OPTS=""
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
if [[ "$JAVA_VERSION" =~ ^1\.8\..*$ ]]; then
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$LOGS_DIR/gc-${POD_IP}-$(date '+%s').log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-${POD_IP}-$(date '+%s').hprof"
elif [[ "$JAVA_VERSION" =~ ^11\..*$ ]]; then
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -Xlog:gc:$LOGS_DIR/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-${POD_IP}-$(date '+%s').hprof"
else
    echo "The JAVA_MEM_OPTS parameter does not support setting in this Java version $JAVA_VERSION."
fi

# 配置调试和JMX参数
JAVA_DEBUG_OPTS=""
JAVA_JMX_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=47.116.33.28 -Dcom.sun.management.jmxremote.rmi.port=1199"
fi
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

# 打印服务启动参数配置信息
echo "---------------------------------"
echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
echo "JAVA_DEBUG_OPTS: $JAVA_DEBUG_OPTS"
echo "JAVA_JMX_OPTS: $JAVA_JMX_OPTS"
echo "---------------------------------"

# 启动服务
echo -e "Starting the $SERVER_NAME ...\c"
JAR_NAME="$SERVER_NAME-$SERVER_ENV.jar" # JAR包名称
nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -jar "$DEPLOY_DIR/$JAR_NAME" --spring.profiles.active="$SERVER_ENV" --server.port="$SERVER_PORT" > "$DEPLOY_DIR/nohup.out" 2>&1 &

# 等待服务启动
COUNT=0
START_TIME=$(date +%s)  # 记录开始时间
TIMEOUT=60              # 设置超时时间
while [ "$COUNT" -lt 1 ]; do
    ELAPSED_TIME=$(( $(date +%s) - $START_TIME ))
    # 如果超出了指定时间，则退出循环
    if [ "$ELAPSED_TIME" -ge "$TIMEOUT" ]; then
        echo "INTERRUPT(${ELAPSED_TIME}s)"
        exit 1
    fi
    # 开始进行服务启动轮询监听
    echo -e ".\c"
    sleep 1
    if [ -n "$SERVER_PORT" ]; then
        COUNT=$(netstat -an | grep -c "$SERVER_PORT")
    else
        COUNT=`ps -f | grep java | grep -v grep | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    fi
    if [ "$COUNT" -gt 0 ]; then
        echo "OK(${ELAPSED_TIME}s)"
        break
    fi
done

PIDS=`ps -f | grep java | grep -v grep | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "NOHUP_OUT: $DEPLOY_DIR/nohup.out"
