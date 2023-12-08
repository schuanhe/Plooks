FROM openjdk:17-slim

WORKDIR /app

# 检查是否存在plooks-api-1.0-SNAPSHOT.jar文件
RUN if [ ! -e target/plooks-api-1.0-SNAPSHOT.jar ]; then \
    echo "Error: 请先打包jar文件，或者使用Dockerfile-build"; \
    exit 1; \
    fi

# 复制JAR文件
COPY target/plooks-api-1.0-SNAPSHOT.jar .

EXPOSE 2023

CMD ["java", "-jar", "plooks-api-1.0-SNAPSHOT.jar"]
