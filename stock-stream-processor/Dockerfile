FROM openjdk:8-jdk-alpine AS base_alpine_jdk8

ENV PROCESSOR_FILE stock-stream-processor-latest.jar
ENV PROCESSOR_HOME /stock-stream-processor/build/libs

ADD build/libs/$PROCESSOR_FILE $PROCESSOR_HOME/$PROCESSOR_FILE

EXPOSE 8080

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $PROCESSOR_HOME/$PROCESSOR_FILE --spring.data.mongodb.uri=$DB_HOST --spring.cloud.stream.kafka.binder.brokers=$KAFKA_HOST"]
