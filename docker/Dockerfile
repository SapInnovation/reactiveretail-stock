FROM openjdk:8-jre-alpine

ENV VERTICLE_FILE stock-exposer-latest.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/opt

EXPOSE 8080

# Copy your fat jar to the container
RUN ls -ltrh
COPY ../build/libs/$VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
