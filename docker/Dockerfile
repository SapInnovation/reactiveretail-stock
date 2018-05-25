FROM openjdk:8-jre-alpine

ENV VERTICLE_FILE stock-exposer-latest.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/opt

EXPOSE 8080

# Copy your fat jar to the container
RUN  apk update
RUN  apk add git
RUN  git clone https://github.com/SapInnovation/reactiveretail-stock.git
RUN  cd reactiveretail-stock
RUN  ls -ltrh
RUN  ./gradlew build:stock-exposer
COPY ./docker/$VERTICLE_FILE $VERTICLE_HOME/


# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
