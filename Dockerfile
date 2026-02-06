# 建议使用基于 alpine 的版本，体积更小
FROM eclipse-temurin:17-jre-alpine

# Alpine 使用 apk 安装工具，如果换成 focal 则继续用 apt
RUN apk add --no-cache ttyd coreutils

WORKDIR /app

# 这里的路径指向 Action 构建时产生的临时位置
COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

EXPOSE 7681
ENV BASE_PATH="/"

# 启动命令
ENTRYPOINT ["sh", "-c", "timeout 1800s ttyd -p 7681 -once -a -base-path ${BASE_PATH} java -jar app.jar"]