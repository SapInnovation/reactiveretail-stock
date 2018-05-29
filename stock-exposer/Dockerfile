# Stage 0
FROM openjdk:8-jdk-alpine AS base_alpine_jdk8

# build jar
RUN  apk update && \
     apk add git
     git clone https://github.com/SapInnovation/reactiveretail-stock.git
RUN  cd reactiveretail-stock && ./gradlew :stock-exposer:assemble --no-daemon

# Stage 1
FROM openjdk:8-jdk-alpine
ENV EXPOSER_FILE stock-exposer-latest.jar
ENV EXPOSER_HOME /reactiveretail-stock/stock-exposer/build/libs

# Copy jar from previous stage
COPY --from=base_alpine_jdk8 $EXPOSER_HOME/$EXPOSER_FILE $EXPOSER_FILE

EXPOSE 8080

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $EXPOSER_FILE --spring.data.mongodb.uri=$DB_HOST"]
