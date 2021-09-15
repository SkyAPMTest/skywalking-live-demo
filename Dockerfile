FROM ghcr.io/apache/skywalking-java/jdk-8:latest

ARG TARGET_JAR

COPY ${TARGET_JAR} /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
