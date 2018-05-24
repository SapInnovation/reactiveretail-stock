
FROM openjdk:8-jdk-alpine

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS

ADD stock-exposer-latest.jar stock-exposer.jar
EXPOSE 8080

# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar stock-exposer.jar
