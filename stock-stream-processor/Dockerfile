
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ADD build/libs/stock-stream-processor-0.0.1.jar stock-stream-processor.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar stock-stream-processor.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar stock-stream-processor.jar
