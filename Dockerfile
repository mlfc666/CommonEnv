FROM eclipse-temurin:17-jre-alpine

# 1. 核心修复：安装 libaio, numactl 和 glibc 兼容层 (gcompat)
# MariaDB4j 需要 libaio 来进行异步 I/O，gcompat 解决 Alpine 的 musl libc 兼容性问题
RUN apk add --no-cache \
    ttyd \
    coreutils \
    tmux \
    libaio \
    numactl \
    gcompat

WORKDIR /app

# 复制可执行文件
COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

# 暴露 ttyd 端口
EXPOSE 7681

# 默认环境变量
ENV BASE_PATH="/"

# 保持 ENTRYPOINT 不变
ENTRYPOINT ["sh", "-c", "ttyd -p 7681 -a -base-path ${BASE_PATH} tmux new-session -A -s common_env 'java -Xmx192m -Xms128m -XX:+UseSerialGC -jar /app/app.jar'"]