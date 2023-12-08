# 构建阶段
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean install

# 运行阶段
FROM openjdk:17-jre-slim

WORKDIR /app

# 复制构建阶段产生的 JAR 文件
COPY --from=build /app/target/plooks-api-1.0-SNAPSHOT.jar .

CMD ["java", "-jar", "plooks-api-1.0-SNAPSHOT.jar"]