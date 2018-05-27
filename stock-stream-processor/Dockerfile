FROM openjdk:8-jdk-alpine

ENV PROCESSOR_FILE stock-stream-processor-latest.jar
EXPOSE 8082

# build jar
RUN  apk update
RUN  apk add git
RUN  git clone https://github.com/SapInnovation/reactiveretail-stock.git
RUN  cd reactiveretail-stock && ./gradlew :stock-steam-processor:build --no-daemon -x test
ENV PROCESSOR_HOME /reactiveretail-stock/stock-stream-processor/build/libs

# Launch the application
WORKDIR $PROCESSOR_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $PROCESSOR_FILE"]
