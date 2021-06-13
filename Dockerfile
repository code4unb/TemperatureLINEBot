FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG KEYSTORE_FILE
ARG KEYSTORE_PASSWORD
ENV LINE_BOT_CHANNEL_SECRET xxx
ENV LINE_BOT_CHANNEL_TOKEN xxx
ENV SPRING_PROFILES_ACTIVE prod
ENV SSL_KEYSTORE_PASSWORD ${KEYSTORE_PASSWORD}
ENV SSL_KEYSTORE_PATH /cacerts
COPY key.p12 key.p12
COPY ${JAR_FILE} app.jar
USER root
RUN keytool -importkeystore -srckeystore key.p12 -srcstoretype PKCS12 -srcstorepass ${KEYSTORE_PASSWORD} -alias cert -deststorepass changeit -destkeypass changeit -destkeystore cacerts
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar /app.jar" ]