# Stock Message Processor application 
1. Exposes Rest API endpoints
2. Posts message in Kafka topic
3. Listens to messages from Kafka topic
4. Persists data in Mongo DB

## Requirements

1. Java - 1.8.x
2. Maven - 3.7.x
3. MongoDB - 3.6.x
4. Kafka - v0.10+

## Steps to Setup
**1. Clone the application**

```bash
//TODO: Add Git clone link post repo creation
```

**2. Build and run the app using Maven**

```
1. Maven Clean and Build
mvn spring-boot:run
```

**3. Steps to get running**

```
1. Make changes in application.yaml for Kafka and Mongo DB endpoints
2. On dev-box, server will start at <http://localhost:8080>
```

## Exploring the Rest APIs

The application defines following REST APIs

```
1. POST /stockdata - Push messages in application/json format
```
