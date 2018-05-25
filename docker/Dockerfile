FROM openjdk:8-jdk-alpine

ENV VERTICLE_FILE stock-exposer-latest.jar
EXPOSE 8080

# Copy your fat jar to the container
RUN  apk update
RUN  apk add git
RUN  git clone https://github.com/SapInnovation/reactiveretail-stock.git
RUN  cd reactiveretail-stock && git checkout pipeline
RUN  ls -ltrh
RUN  cd reactiveretail-stock && ./gradlew :stock-exposer:build --no-daemon
ENV VERTICLE_HOME /reactiveretail-stock/stock-exposer/build/libs

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
