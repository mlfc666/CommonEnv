FROM debian:bookworm-slim

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV LANG=C.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jre-headless \
    tmux \
    libaio1 \
    libnuma1 \
    curl \
    ca-certificates \
    && curl -LO https://github.com/tsl0922/ttyd/releases/download/1.7.7/ttyd.x86_64 \
    && chmod +x ttyd.x86_64 \
    && mv ttyd.x86_64 /usr/local/bin/ttyd \
    && apt-get purge -y curl ca-certificates \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build/libs/CommonEnv-SNAPSHOT.jar app.jar

EXPOSE 7681
ENV BASE_PATH="/"

ENTRYPOINT ["sh", "-c", "ttyd -p 7681 -a -base-path ${BASE_PATH} tmux -2 new-session -A -s common_env 'export TERM=xterm-256color; java -Xmx192m -Xms128m -XX:+UseSerialGC -jar /app/app.jar'"]