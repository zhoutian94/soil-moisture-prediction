#!/bin/bash

# 检查是否传入了两个参数
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <REPO_URL> <WORK_DIR>"
  exit 1
fi

# 获取传入的参数
# 代码仓库地址
REPO_URL=$1
# 工作目录
WORK_DIR=$2




# 创建工作目录（如果不存在）
mkdir -p "$WORK_DIR"

# 切换到工作目录
cd "$WORK_DIR" || exit 1

# 克隆最新代码（如果目录不存在）
if [ ! -d "$WORK_DIR/loran_service_front" ]; then
  git clone "$REPO_URL"
  cd "$WORK_DIR/loran_service_front" || exit 1
  # 安装依赖和编译步骤在克隆后立即执行
  npm install 
  npm run build
else
  # 如果目录存在，拉取最新代码并判断是否需要更新
  cd "$WORK_DIR/loran_service_front" || exit 1
  git fetch origin
  if git diff --quiet HEAD origin; then
    echo "Code is already up to date. Skipping npm install and build."
  else	  
    # 如果拉取最新代码
    git pull
    # 删除 dist 目录
    echo "Deleting the 'dist' directory..."
    rm -rf dist
    
    echo "Code has been updated. Running npm install and build."
    npm install
    npm run build:prod
  fi
fi

# 查找http-server进程ID
PID=$(lsof -t -i :8080)

# 如果找到进程ID，则终止这些进程
if [ -n "$PID" ]; then
  echo "Stopping old http-server instance(s)..."
  kill -9 $PID
else
  echo "No old http-server instance found on port $PORT."
fi

# 启动 http-server
echo "Starting http-server..."
nohup http-server dist > server.log 2>&1 &