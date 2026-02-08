# 使用 Ubuntu 22.04 基础镜像
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y \
    software-properties-common \
    && add-apt-repository universe \
    && apt-get update && apt-get install -y \
    ttyd \
    tmux \
    libaio1 \
    libnuma1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 复制你的 Jar 包
COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

EXPOSE 7681
ENV BASE_PATH="/"

ENTRYPOINT ["sh", "-c", "ttyd -p 7681 -a -base-path ${BASE_PATH} tmux new-session -A -s common_env 'java -Xmx192m -Xms128m -XX:+UseSerialGC -jar /app/app.jar'"]