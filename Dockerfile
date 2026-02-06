# 基础镜像
FROM openjdk:17-slim

# 安装必要工具
RUN apt-get update && apt-get install -y ttyd coreutils && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 这里的路径指向 Action 构建时产生的临时位置
COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

EXPOSE 7681
ENV BASE_PATH="/"

# 启动命令
ENTRYPOINT ["sh", "-c", "timeout 1800s ttyd -p 7681 -once -a -base-path ${BASE_PATH} java -jar app.jar"]