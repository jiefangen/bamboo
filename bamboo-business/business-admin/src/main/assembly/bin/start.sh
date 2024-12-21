#!/bin/bash

# 获取当前脚本所在路径，并设定部署目录、配置目录
cd "$(dirname "$0")" || exit 1
cd .. || exit 1
DEPLOY_DIR="$(pwd)"
CONF_DIR="$DEPLOY_DIR/conf"
LOGS_DIR="$DEPLOY_DIR/logs"

# 获取配置文件中的 server name 和端口等信息
#SERVER_NAME=`sed '/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#SERVER_PROTOCOL=`sed '/dubbo.protocol.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#SERVER_HOST=`sed '/dubbo.protocol.host/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#SERVER_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
#LOGS_FILE=`sed '/dubbo.log4j.file/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`

# 设置默认值
SERVER_HOST="${SERVER_HOST:-127.0.0.1}"
SERVER_NAME="${SERVER_NAME:-$(hostname)}"

# 输出配置信息
echo "---------------------------------"
echo "DEPLOY_DIR: $DEPLOY_DIR"
echo "SERVER_NAME: $SERVER_NAME"
echo "SERVER_HOST: $SERVER_HOST"
echo "---------------------------------"

# 检查服务是否已经启动
PIDS=$(pgrep -f "$CONF_DIR" | grep -v grep)
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME is already running with PID(s): $PIDS"
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
# 获取项目服务配置HEAP_SIZE参数（单位：MB）HEAP_SIZE_MB
JVM_HEAP_SIZE="512"
# 判断参数是否有效（128MB到6144MB之间）最小约128MB最大约6G
if [[ -n "$JVM_HEAP_SIZE" && "$JVM_HEAP_SIZE" -ge 128 && "$JVM_HEAP_SIZE" -le 6144 ]]; then
    JVM_HEAP_SIZE="${JVM_HEAP_SIZE}m"
    echo "Using external JVM_HEAP_SIZE: ${JVM_HEAP_SIZE}"
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
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}') # 获取 Java 版本号
if [[ "$JAVA_VERSION" =~ ^1\.[1-9][1-9]*$ || "$JAVA_VERSION" =~ ^[1-9][1-9]*$ ]]; then
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -Xlog:gc:$LOGS_DIR/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-${POD_IP}-$(date '+%s').hprof"
else
    JAVA_MEM_OPTS="-server -Xms$JVM_HEAP_SIZE -Xmx$JVM_HEAP_SIZE -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$LOGS_DIR/gc-${POD_IP}-$(date '+%s').log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR/dump-${POD_IP}-$(date '+%s').hprof"
fi

# 配置调试和JMX参数
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS="-Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
    JAVA_JMX_OPTS="-Djava.rmi.server.hostname=47.116.33.28 -Dcom.sun.management.jmxremote.port=5777 -Dcom.sun.management.jmxremote.rmi.port=5779 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

# 打印服务启动参数配置信息
echo "---------------------------------"
echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
echo "JAVA_DEBUG_OPTS: $JAVA_DEBUG_OPTS"
echo "JAVA_JMX_OPTS: $JAVA_JMX_OPTS"
echo "---------------------------------"

# 启动服务
echo -e "Starting the $SERVER_NAME ...\c"
nohup java "$JAVA_OPTS" "$JAVA_MEM_OPTS" "$JAVA_DEBUG_OPTS" "$JAVA_JMX_OPTS" -jar "$DEPLOY_DIR/$JAR_NAME" > "$DEPLOY_DIR/nohup.out" 2>&1 &

# 等待服务启动
COUNT=0
while [ "$COUNT" -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    if [ -n "$SERVER_PORT" ]; then
        if [ "$SERVER_PROTOCOL" == "dubbo" ]; then
            COUNT=$(echo status | nc -i 1 "$SERVER_HOST" "$SERVER_PORT" | grep -c OK)
        else
            COUNT=$(netstat -an | grep -c "$SERVER_PORT")
        fi
    else
        COUNT=`ps -f | grep java | grep -v grep | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    fi
    if [ "$COUNT" -gt 0 ]; then
        break
    fi
done

echo "Startup Success!"
PIDS=`ps -f | grep java | grep -v grep | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID(s): $PIDS"
echo "STDOUT: $STDOUT_FILE"
