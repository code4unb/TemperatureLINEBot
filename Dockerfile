FROM openjdk:8-jdk-alpine
ENV JAVA_OPTS = "-Dspring.profiles.active=dev"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar /app.jar" ]