FROM openjdk:11-jdk-slim
ARG JAR_FILE
ENV LINE_BOT_CHANNEL_SECRET xxx
ENV LINE_BOT_CHANNEL_TOKEN xxx
ENV SPRING_PROFILES_ACTIVE prod
ENV POSTGRES_DB LineBot_Data
ENV PRODUCTION prod
ENV POSTGRES_PASSWORD xxx
COPY build/libs/${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar /app.jar" ]