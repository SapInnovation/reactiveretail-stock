FROM openjdk:8-jdk-alpine

ENV EXPOSER_FILE stock-exposer-latest.jar
EXPOSE 8081

# build jar
RUN  apk update
RUN  apk add git
RUN  git clone https://github.com/SapInnovation/reactiveretail-stock.git
RUN  cd reactiveretail-stock && ./gradlew :stock-exposer:build --no-daemon -x test
ENV EXPOSER_HOME /reactiveretail-stock/stock-exposer/build/libs

# Launch the application
WORKDIR $EXPOSER_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $EXPOSER_FILE"]
