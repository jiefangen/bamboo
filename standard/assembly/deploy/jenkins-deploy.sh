#!/bin/bash

# ------------------------------
# 项目解压脚本
# 用于本地或 Jenkins 环境解压指定项目的压缩包
# ------------------------------

# 本地调试目录（绝对路径）
LOCAL_DIR="/Users/fangen/Documents/IntelliJ_IDEA/projects/github/bamboo/bamboo-business/business-admin/target"

# 目标目录，根据参数决定（默认为本地目录）
TARGET_DIR=""

# 判断参数，如果第一个参数是 "jenkins"，则切换到 Jenkins 部署的目标目录
if [ "${1:-local}" = "jenkins" ]; then
    TARGET_DIR=`pwd`
else
    TARGET_DIR="$LOCAL_DIR"
fi

# 输出配置信息
echo "---------------------------------"
echo "环境参数：${1:-local}"
echo "目标目录：$TARGET_DIR"
echo "---------------------------------"

# 检查目标目录是否存在
if [ ! -d "$TARGET_DIR" ]; then
    echo "错误：目标目录 $TARGET_DIR 不存在！"
    exit 1
fi

# 进入目标目录
cd "$TARGET_DIR" || { echo "错误：无法进入目录 $TARGET_DIR！"; exit 1; }

# 获取压缩包文件名（假设只有一个压缩包）
ARCHIVE_NAME=$(ls *-assembly.tar.gz 2>/dev/null)
# 检查压缩文件是否存在
if [ ! -f "$ARCHIVE_NAME" ]; then
    echo "错误：文件 $ARCHIVE_NAME 不存在！"
    exit 1
fi

# 从压缩包文件名中提取项目名称
PROJECT_NAME=$(echo "$ARCHIVE_NAME" | sed -E 's/-assembly//;s/.tar.gz//')

# 解压前清理旧文件夹
echo "清理旧文件夹：$PROJECT_NAME-assembly"
rm -rf "$PROJECT_NAME-assembly"

# 解压压缩包
echo "开始解压 $ARCHIVE_NAME..."
tar -zxvf "$ARCHIVE_NAME"
# 检查解压结果
if [ $? -eq 0 ]; then
    echo "解压完成 $ARCHIVE_NAME"
else
    echo "错误：解压失败！"
    exit 1
fi

# 查询遍历要复制的JAR文件
JAR_FILES_TO_COPY=$(find "$TARGET_DIR/$PROJECT_NAME-assembly/jar" -name "*.jar")
# 判断是否存在JAR文件
if [ -z "$JAR_FILES_TO_COPY" ]; then
    echo "警告：未找到任何.jar文件，跳过复制。"
else
    # 遍历查找到的文件并复制
    for file in $JAR_FILES_TO_COPY; do
        if [ -f "$file" ]; then
            cp "$file" "$TARGET_DIR"
            echo "复制JAR文件成功：$file -> $TARGET_DIR"
        else
            echo "警告：JAR文件不存在，跳过复制：$file"
        fi
    done
fi

# 定义一个复制文件夹的函数
copy_files() {
    SOURCE_DIR="$1"
    TARGET_DIR="$2"
    if [ -d "$SOURCE_DIR" ]; then
        # 创建目标目录（如果不存在）
        mkdir -p "$TARGET_DIR"
        cp -rf "$SOURCE_DIR/"* "$TARGET_DIR"
        if [ $? -eq 0 ]; then
            echo "复制文件夹成功：$SOURCE_DIR -> $TARGET_DIR"
        else
            echo "复制 $SOURCE_DIR 下文件时出现错误！"
        fi
    else
        echo "警告：未找到 $SOURCE_DIR 目录，跳过复制。"
    fi
}

# 设置目录路径
BIN_DIR="$TARGET_DIR/$PROJECT_NAME-assembly/bin"
CONF_DIR="$TARGET_DIR/$PROJECT_NAME-assembly/conf"
TARGET_BIN_DIR="$TARGET_DIR/bin"
TARGET_CONF_DIR="$TARGET_DIR/conf"
# 调用函数复制 bin 和 conf 目录
copy_files "$BIN_DIR" "$TARGET_BIN_DIR"
copy_files "$CONF_DIR" "$TARGET_CONF_DIR"

echo "---------------------------------"
echo "脚本执行完成！"
echo "---------------------------------"
