#使用更轻量的运行时镜像
FROM maven:3.5-jdk-8-alpine as builder

#将编译好的 JAR 文件复制到镜像中
COPY /target/userBackend-0.0.1-SNAPSHOT.jar /app/target/userBackend-0.0.1-SNAPSHOT.jar

#运行容器时启动的命令
CMD ["java","-jar","/app/target/userBackend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]

LABEL authors="Solar_Rain"