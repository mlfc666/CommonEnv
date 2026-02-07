FROM eclipse-temurin:17-jre-alpine

# 安装必要工具：ttyd (Web终端), tmux (会话保持), coreutils (系统工具)
RUN apk add --no-cache ttyd coreutils tmux

WORKDIR /app

# 复制可执行文件
COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

# 暴露 ttyd 端口
EXPOSE 7681

# 默认环境变量
ENV BASE_PATH="/"

# 去掉 -once 参数，防止刷新导致进程退出。
ENTRYPOINT ["sh", "-c", "ttyd -p 7681 -a -base-path ${BASE_PATH} tmux new-session -A -s common_env 'java -Xmx192m -Xms128m -XX:+UseSerialGC -jar /app/app.jar'"]