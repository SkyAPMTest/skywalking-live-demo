FROM ghcr.io/skyapmtest/agent

ARG TARGET_JAR

COPY ${TARGET_JAR} /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
