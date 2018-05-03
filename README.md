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

./gradlew :stock-exposer:bootRun

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
