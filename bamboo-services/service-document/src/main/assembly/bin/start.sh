#!/bin/bash

# 获取当前脚本所在路径，并设定部署目录、配置目录
cd "$(dirname "$0")" || exit 1
cd .. || exit 1
DEPLOY_DIR="$(pwd)"
LOGS_DIR="$DEPLOY_DIR/logs"

# 设置日志文件夹
if [ ! -d "$LOGS_DIR" ]; then
    mkdir -p "$LOGS_DIR" || { echo "ERROR: Failed to create log directory"; exit 1; }
fi
STDOUT_FILE="$LOGS_DIR/stdout.log"
get_timestamp() { # 获取当前时间的函数
    date "+%Y-%m-%d %H:%M:%S"
}
# 记录脚本开始的时间
START_TIME=$(date +%s)
echo "$(get_timestamp) - INFO [$(basename $0)] execution started." >> "$STDOUT_FILE"

# 从配置文件中提取服务运行的配置信息
SERVER_NAME=$(awk -F '=' '/application.name/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
SERVER_ENV=$(awk -F '=' '/profiles.active/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
JAR_NAME=$(awk -F '=' '/application.jar/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
SERVER_PORT=$(grep -A 1 'server:' "conf/application-$SERVER_ENV.yml" | grep 'port' | sed 's/.*: *//')
HEAP_SIZE_MB=$(awk -F '=' '/heap.size/ {gsub(/\r/, ""); print $2}' conf/maven.properties)
JMX_RMI_IP=$(awk -F '=' '/rmi.hostname/ {gsub(/\r/, ""); print $2}' conf/maven.properties)

# 检查是否有为空的变量
if [ -z "$SERVER_NAME" ]; then
    echo "ERROR: SERVER_NAME (application.name) is missing or empty!"
    exit 1
fi
if [ -z "$SERVER_ENV" ]; then
    echo "ERROR: SERVER_ENV (profiles.active) is missing or empty!"
    exit 1
fi
if [ -z "$JAR_NAME" ]; then
    echo "ERROR: JAR_NAME (application.jar) is missing or empty!"
    exit 1
fi

# 输出配置信息
echo "-------------- MAVEN_CONFIG -------------------"
echo "SERVER_NAME: $SERVER_NAME"
echo "SERVER_ENV: $SERVER_ENV"
echo "JAR_NAME: $JAR_NAME"
echo "SERVER_PORT: $SERVER_PORT"
echo "HEAP_SIZE_MB: $HEAP_SIZE_MB"
echo "-------------- MAVEN_CONFIG -------------------"

# 检查服务是否已经启动
PIDS=$(pgrep -f "$DEPLOY_DIR" | grep -v grep)
if [ -n "$PIDS" ]; then
    echo "WARN: The $SERVER_NAME is already running with PID: $PIDS"
    echo "$(get_timestamp) - WARN [$(basename $0)] The $SERVER_NAME is already running with PID: $PIDS" >> "$STDOUT_FILE"
    exit 1
fi

# 检查端口是否被占用
if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=$(netstat -tln | grep -c "$SERVER_PORT")
    if [ "$SERVER_PORT_COUNT" -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT is already in use!"
        echo "$(get_timestamp) - ERROR [$(basename $0)] The $SERVER_NAME port $SERVER_PORT is already in use!" >> "$STDOUT_FILE"
        exit 1
    fi
fi

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
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -XX:+DisableExplicitGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$LOGS_DIR/gc-$(date '+%s').log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-$(date '+%s').hprof"
elif [[ "$JAVA_VERSION" =~ ^11\..*$ ]]; then
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -XX:+DisableExplicitGC -Xlog:gc:$LOGS_DIR/gc-$(date '+%s').log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-$(date '+%s').hprof"
else
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-$(date '+%s').hprof"
fi

# 配置debug调试参数
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
  DEBUG_PORT="8000" # 默认DEBUG端口为8000
  if [ -n "$SERVER_PORT" ]; then
    DEBUG_PORT=$((SERVER_PORT + 500))
  fi
  if [[ "$JAVA_VERSION" =~ ^1\.8\..*$ ]]; then
      JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT"
  else
      JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT"
  fi
fi
# 配置JMX参数
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JMX_PORT="1099"
    RMI_PORT="1199"
    if [ -n "$SERVER_PORT" ]; then
        JMX_PORT=$((SERVER_PORT + 100))
        RMI_PORT=$((JMX_PORT + 200))
    fi
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
    if [ -n "$JMX_RMI_IP" ]; then
        JAVA_JMX_RMI="-Djava.rmi.server.hostname=$JMX_RMI_IP -Dcom.sun.management.jmxremote.rmi.port=$RMI_PORT"
        JAVA_JMX_OPTS="$JAVA_JMX_OPTS $JAVA_JMX_RMI"
    fi
fi

# 打印服务启动参数配置信息
echo "-------------- JAVA_OPTS -------------------"
echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
echo "JAVA_DEBUG_OPTS: $JAVA_DEBUG_OPTS"
echo "JAVA_JMX_OPTS: $JAVA_JMX_OPTS"
echo "-------------- JAVA_OPTS -------------------"

# 启动服务
echo -e "Starting the $SERVER_NAME ...\c"
if [ -n "$SERVER_PORT" ]; then
  nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -jar "$DEPLOY_DIR/$JAR_NAME" --spring.profiles.active="$SERVER_ENV" --server.port="$SERVER_PORT" > "$DEPLOY_DIR/nohup.out" 2>&1 &
else
  nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -jar "$DEPLOY_DIR/$JAR_NAME" --spring.profiles.active="$SERVER_ENV" > "$DEPLOY_DIR/nohup.out" 2>&1 &
fi

# 等待服务启动
COUNT=0
START_TIME=$(date +%s)  # 记录开始时间
TIMEOUT=60              # 设置超时时间
while [ "$COUNT" -lt 1 ]; do
    ELAPSED_TIME=$(( $(date +%s) - $START_TIME ))
    # 如果超出了指定时间，则退出循环
    if [ "$ELAPSED_TIME" -ge "$TIMEOUT" ]; then
        echo "INTERRUPT(${ELAPSED_TIME}s)"
        echo "$(get_timestamp) - WARN [$(basename $0)] Start timeout interrupt(${ELAPSED_TIME}s)" >> "$STDOUT_FILE"
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
        echo "OK!"
        break
    fi
done

PIDS=`ps -f | grep java | grep -v grep | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "NOHUP_OUT: $DEPLOY_DIR/nohup.out"

# 记录脚本结束的时间
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))
echo "$(get_timestamp) - INFO [$(basename $0)] execution ended. Total execution time: $DURATION seconds." >> "$STDOUT_FILE"
