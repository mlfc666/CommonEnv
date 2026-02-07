#!/bin/sh

# 定义 tmux 会话名称
SESSION_NAME="common_env"

# 启动 ttyd 并让其连接到 tmux 会话
# -A 表示：如果会话存在则连接（Attach），不存在则创建
exec timeout 1800s ttyd -p 7681 -once -a -base-path ${BASE_PATH} \
    tmux new-session -A -s $SESSION_NAME "java -jar /app/app.jar"