# Capital-Compass - Stock Microservice

## Description

This microservice, as part of the Capital Compass application, is designed for interacting with polygon.io's APIs. It
focuses on fetching tickers, ticker prices, and storing ticker details, using Spring Boot WebFlux.

## Installation

**Prerequisites:**

- JDK 11
- Gradle 8.4
- Access to AWS Parameter Store and AWS Secrets Manager
- Configured MySQL database

**Steps:**

1. Clone the repository: `git clone git@github.com:samuelmlee/capital-compass-stock-service.git`
2. Navigate to the project directory: `cd capital-compass-stock-service`
3. Build the project: `./gradlew build`
4. Run the application: `./gradlew bootRun`

## Technologies

- **Spring Boot WebFlux**: For non-blocking applications
- **Java**: Version 11
- **Gradle**: For dependency management and project building.
- **MySQL**: For storing ticker details
- **AWS Services (Parameter Store, Secrets Manager)**: For fetching remote configurations

## Dependencies / Libraries

- **Spring WebFlux**
- **Project Reactor**
- **polygon.io Spring WebFlux Client**: For Polygon.io API interactions.

Dependencies are managed through Gradle.

## Usage

Upon launching, the service is accessible through the API Gateway, ex: `https://localhost:8082`.

**API Endpoints:**

Endpoints are listed at http://localhost:8083/swagger-ui.html. These include:

- `GET /v1/stocks/reference/tickers/details/{symbol}`: Retrieves ticker details for a specified symbol.

## Release History

- **0.0.1** - Initial release: Basic functionality for fetching and storing ticker details implemented.

