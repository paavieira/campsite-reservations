# campsite-reservations

## Overview

This is an example of a Spring Boot REST API service to manage campsite reservations.

The service follows the Onion Architecture, and it's split in three different layers:

- **domain**:
  The domain layer is the inner-most layer of the application. It defines behavior and constraints of business logic;
- **application**:
  The application layer orchestrates the use of the entities from the domain layer, and it adapts requests (commands and queries) coming
  from
  the framework layer by sitting between the two;
- **frawework**:
  The framework layer sits outside the application layer and it integrates with third-party frameworks and libraries (e.g. Spring Data
  MongoDB repositories).

## Dependencies

- Docker & docker-compose
- MongoDB
- JDK 17

## Building the app

To build (and test) the application, run the following:

```bash
./gradlew build
```

## Running the app

In order to run, the application requires a MongoDB replica set. The `docker-compose.yml` file describes that dependency.

To start the MongoDB replica set and run the application, execute the following commands:

```shell
./gradlew build
docker build -t com.paavieira.campsites/reservations .
docker-compose up
```

Alternatively, run the `run.sh` script (which is a wrapper for those commands).

```shell
./run.sh
```

## Validating the app

To check if everything is fine, run the following:

```
curl --location --request POST 'http://localhost:8080/availabilities' \
--header 'Content-Type: application/json' \
--data-raw '{
    "from": "2022-09-30"
}'
```

The expected result is something like this:

```json
{"from":"2022-09-30","to":"2022-10-30","available":true}
```

In order to make it easier to test the REST API, import the `postman_collection.json` into [Postman](https://www.postman.com/).

## API docs

With the application running, you can check the API docs at the following URL:

http://localhost:8080/swagger-ui/index.html
