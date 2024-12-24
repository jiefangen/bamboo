#!/bin/bash

cd "$(dirname "$0")" || exit 1
BIN_DIR="$(pwd)"
cd .. || exit 1
DEPLOY_DIR="$(pwd)"

SERVER_NAME=`sed '/application.name/!d;s/.*=//' conf/maven.properties | tr -d '\r'`
SERVER_ENV=`sed '/profiles.active/!d;s/.*=//' conf/maven.properties | tr -d '\r'`
SERVER_PORT=$(grep -A 1 'server:' "conf/application-$SERVER_ENV.yml" | grep 'port' | sed 's/.*: *//')

if [ -z "$SERVER_NAME" ]; then
	SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep -v grep | grep "$DEPLOY_DIR" |awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "WARN: The $SERVER_NAME does not started!"
    exit 1
fi

if [ "$1" != "skip" ]; then
    $BIN_DIR/dump.sh
fi

echo -e "Stopping the $SERVER_NAME ...\c"
for PID in $PIDS ; do
    kill $PID > /dev/null 2>&1
done

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in $PIDS ; do
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        else
          if [ -n "$SERVER_PORT" ]; then
              SERVER_PORT_COUNT=$(netstat -tln | grep -c "$SERVER_PORT")
              if [ "$SERVER_PORT_COUNT" -gt 0 ]; then
                   COUNT=0
                   break
              fi
          fi
        fi
    done
done

echo "OK!"
echo "PID: $PIDS"
