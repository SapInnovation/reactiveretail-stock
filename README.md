# Reactive Stock Exposer Service exposing Rest APIs with Spring WebFlux and Reactive MongoDB

## Requirements

1. Java - 1.8.x
2. Gradle - 4.7
3. MongoDB - 3.x.x

## Steps to Setup
**1. Clone the application**

```bash
//TODO: Add Git clone link post repo creation
```

**2. Build and run the app using Gradle**

```
./gradlew :stock-exposer:bootRun
```
On dev-box, server will start at <http://localhost:8080> connecting to localhost Mongo DB setup at 27017 port.

## Exploring the Rest APIs

The application defines following REST APIs

```
1. GET /stock - Get Stock for all products

2. POST /stock - Persist stock for a new product

3. GET /stock/{productId} - Retrieve Stock Info by Product Id

3. PUT /stock/{productId} - Update Stock Info by Product Id

4. GET /stream/stock - Stream stock updates to a browser as Server-Sent Events
```


## Running Stock Message Generator

```
1. Maven Clean and Build
mvn spring-boot:run

2. Setup the Kafka Connection in Application.yaml

3. To Post Messages to the Kafka Stream:
http://localhost:8080/stockdata?message=varun

The endpoint can take a JSON object as part of request

4. PostMan script to Dynamically Change the Value and Post -- TBD

```