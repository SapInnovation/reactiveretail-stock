# Stage 1
FROM openjdk:8-jdk-alpine AS base_alpine_jdk8

ENV EXPOSER_FILE stock-exposer-latest.jar
ENV EXPOSER_HOME /stock-exposer/build/libs

# Copy jar from previous stage
ADD stock-exposer /stock-exposer

RUN ls $EXPOSER_HOME/$EXPOSER_FILE

EXPOSE 8080

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $EXPOSER_HOME/$EXPOSER_FILE --spring.data.mongodb.uri=$DB_HOST"]
