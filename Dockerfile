# 建议使用基于 alpine 的版本，体积更小
FROM eclipse-temurin:17-jre-alpine

# 安装 ttyd 和 tmux
RUN apk add --no-cache ttyd coreutils tmux

WORKDIR /app

COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

EXPOSE 7681
ENV BASE_PATH="/"

# 拷贝启动脚本
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# 运行自定义脚本
ENTRYPOINT ["/entrypoint.sh"]